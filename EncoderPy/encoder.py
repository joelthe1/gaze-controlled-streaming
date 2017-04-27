import argparse
import itertools
import multiprocessing
import os

import skvideo.io
import skvideo.motion
from joblib import Parallel, delayed

from image_dct import *

params = {}
np.set_printoptions(precision=2)


def find_dct_coeff_single(img):
    """
    Returns a flattened list of dct coeffs of a single channel image
    :param img: single channel image of shape (h, w)
    :return: flattened list of dct coeffs as strings
    """
    assert img.shape == (544, 960), "Unexpected shape for single channel image"
    return dct8(img)._compute_dct()


def frame_worker(pixels, ind):
    """
    Worker that takes a shared array of bytes and an index for rgb image.
    :param pixels: byte array of all rgb images
    :param ind: starting location of the image in the byte array
    :return: rows of csv
    """
    print("started worker for frame:{}/{}".format(ind // (960 * 540 * 3) + 1, len(pixels) // (960 * 540 * 3)))
    h = 540
    w = 960
    channels = 3
    calculate_motion_vec = (ind >= w * h * 3)
    img_array = np.zeros(shape=(channels, h + 4, w), dtype=np.uint8)
    current_img = np.zeros(shape=(h + 4, w, channels), dtype=np.uint8)
    reference_img = np.zeros(shape=(h + 4, w, channels), dtype=np.uint8)
    for y in range(h + 4):
        for x in range(w):
            if y >= h:
                r, g, b = 0, 0, 0
                pr, pg, pb = 0, 0, 0
            else:
                r, g, b = pixels[ind], pixels[ind + w * h], pixels[ind + w * h * 2]
                # if not the first frame
                # find the previous frame and calculate the motion vectors for current frame
                if calculate_motion_vec:
                    prev_img_ind = ind - (w * h * 3)
                    pr, pg, pb = pixels[prev_img_ind], pixels[prev_img_ind + w * h], pixels[prev_img_ind + w * h * 2]
                ind += 1
            img_array[0][y][x] = r
            img_array[1][y][x] = g
            img_array[2][y][x] = b
            current_img[y][x] = np.array([r, g, b], dtype=np.uint8)
            if calculate_motion_vec:
                reference_img[y][x] = np.array([pr, pg, pb], dtype=np.uint8)

    if calculate_motion_vec:
        frames = np.array([reference_img, current_img])
        motion_vec = skvideo.motion.blockMotion(frames)[0]
        motion_vec_magnitudes = np.linalg.norm(motion_vec, axis=2).flatten()

    r_coeffs = find_dct_coeff_single(img_array[0])
    g_coeffs = find_dct_coeff_single(img_array[1])
    b_coeffs = find_dct_coeff_single(img_array[2])

    rows = []
    for i in range(0, len(r_coeffs), 64):
        row = np.zeros(shape=(3+3*64,), dtype=np.float32)
        frame_no = ind // (960 * 540 * 3)
        block_no = i // 64
        segment = motion_vec_magnitudes[block_no] if calculate_motion_vec else 0.
        row[0] = frame_no
        row[1] = block_no
        row[2] = segment
        row[3:3+64] = b_coeffs[i: i+64]
        row[3+64:3+64*2] = g_coeffs[i: i+64]
        row[3+64*2:] = r_coeffs[i: i+64]
        rows.append(row)

    return rows


def main():
    parser = argparse.ArgumentParser(formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser.add_argument(
        "-i", "--inp",
        default='/Users/jmathai/multimedia_data/oneperson.rgb',
        help="Directory with rgb video."
    )
    parser.add_argument(
        "-o", "--out",
        default='/Users/jmathai/multimedia_data/output',
        help="Directory to write results to"
    )
    parser.add_argument(
        "-n", "--njobs",
        default=multiprocessing.cpu_count(),
        help="no of threads to use"
    )
    w = 960
    h = 540
    channels = 3
    args = parser.parse_args()
    assert os.path.exists(args.inp), "Input dir not valid."
    if not os.path.exists(args.out): os.makedirs(args.out)
    params['input'] = args.inp
    params['output'] = args.out
    params['njobs'] = args.njobs
    pixels = np.fromfile(params["input"], dtype=np.uint8)
    output_file = "{}/{}_encoder_output.npy".format(params["output"], params["input"].split('/')[-1].split('.')[0])

    values = Parallel(n_jobs=params["njobs"])(delayed(frame_worker)(pixels, index)
                                              for index in range(0, len(pixels), (h * w * channels)))

    print("writing to ", output_file)
    np.array(values).flatten().tofile(output_file)


if __name__ == "__main__":
    main()

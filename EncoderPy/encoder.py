import argparse
import itertools
import multiprocessing
import os

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
    return dct8(img)._compute_dct().astype(np.string_)


def frame_worker(pixels, ind):
    """
    Worker that takes a shared array of bytes and an index for rgb image.
    :param pixels: byte array of all rgb images
    :param ind: starting location of the image in the byte array
    :return: rows of csv
    """
    print("started worker for frame:{}/{}".format(ind // (960 * 540 * 3), len(pixels) // (960 * 540 * 3)))
    h = 540
    w = 960
    channels = 3
    img_array = np.zeros(shape=(channels, h + 4, w), dtype=np.uint8)

    for y in range(h + 4):
        for x in range(w):
            if y >= h:
                r, g, b = 0, 0, 0
            else:
                r, g, b = pixels[ind], pixels[ind + w * h], pixels[ind + w * h * 2]
                ind += 1
            img_array[0][y][x] = r
            img_array[1][y][x] = g
            img_array[2][y][x] = b

    r_coeffs = find_dct_coeff_single(img_array[0])
    g_coeffs = find_dct_coeff_single(img_array[1])
    b_coeffs = find_dct_coeff_single(img_array[2])

    rows = []
    for i in range(0, len(r_coeffs), 64):
        rows.append('{},{},{},{},{},{}\n'.format(ind // (960 * 540 * 3),
                                                 i / 64,
                                                 ','.join(r_coeffs[i: i + 64]),
                                                 ','.join(g_coeffs[i: i + 64]),
                                                 ','.join(b_coeffs[i: i + 64]),
                                                 1))
    return rows


def main():
    parser = argparse.ArgumentParser(formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser.add_argument(
        "-i", "--inp",
        default='/Users/jmathai/multimedia_data/final.rgb',
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

    values = Parallel(n_jobs=params["njobs"])(delayed(frame_worker)(pixels, index)
                                              for index in range(0, len(pixels), (h * w * 3)))

    with open('{}/output.csv'.format(params["output"]), 'w') as f:
        for row in list(itertools.chain.from_iterable(values)):
            f.write(row)


if __name__ == "__main__":
    main()

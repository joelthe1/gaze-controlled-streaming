import argparse
import os
from PIL import Image
from image_dct import *
from joblib import Parallel, delayed
import multiprocessing

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

    ind = 0
    frame = 0
    while ind < len(pixels):
        print("Frame:{}/{}".format(frame, len(pixels) // (h * w * 3)))
        # img = np.zeros(shape=(h + 4, w, channels), dtype=np.uint8)
        img_array = np.zeros(shape=(channels, h + 4, w), dtype=np.uint8)

        for y in range(h + 4):
            for x in range(w):
                if y >= h:
                    r, g, b = 0, 0, 0
                else:
                    r, g, b = pixels[ind], pixels[ind + w * h], pixels[ind + w * h * 2]
                    ind += 1
                # img[y][x] = np.array([r, g, b])
                img_array[0][y][x] = r
                img_array[1][y][x] = g
                img_array[2][y][x] = b

        ind = ind + h * w * 2
        # if frame%50==0: Image.fromarray(img, mode="RGB").save('{}/{}.jpg'.format(params["output"], frame))
        r_coeffs = find_dct_coeff_single(img_array[0])
        g_coeffs = find_dct_coeff_single(img_array[1])
        b_coeffs = find_dct_coeff_single(img_array[2])

        # write coeffs into a csv
        # :::::format:::::
        # frame_no, block_no, r_c1, r_c2...r_64, g_1...g_64, b_1...b64, segment

        with open("{}/dct.csv".format(params["output"]), "a") as f:
            for i in range(0, len(r_coeffs), 64):
                row = '{},{},{},{},{},{}\n'.format(frame,
                                                   i / 64,
                                                   ','.join(r_coeffs[i: i + 64]),
                                                   ','.join(g_coeffs[i: i + 64]),
                                                   ','.join(b_coeffs[i: i + 64]),
                                                   1)
                f.write(row)
            f.close()
        frame += 1


if __name__ == "__main__":
    main()

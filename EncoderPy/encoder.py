import argparse
import os
from PIL import Image

from image_dct import *

params = {}
np.set_printoptions(precision=2)


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

    w = 960
    h = 540
    channels = 3

    args = parser.parse_args()
    assert os.path.exists(args.inp), "Input dir not valid."
    if not os.path.exists(args.out): os.makedirs(args.out)
    params['input'] = args.inp
    params['output'] = args.out

    pixels = np.fromfile(params["input"], dtype=np.uint8)

    ind = 0
    frame = 0
    while ind < len(pixels):

        img = np.zeros(shape=(h + 4, w, channels), dtype=np.uint8)
        img_array_r = np.zeros(shape=(h + 4, w), dtype=np.uint8)
        img_array_g = np.zeros(shape=(h + 4, w), dtype=np.uint8)
        img_array_b = np.zeros(shape=(h + 4, w), dtype=np.uint8)

        for y in range(h + 4):
            for x in range(w):
                if y >= h:
                    r, g, b = 0, 0, 0
                else:
                    r, g, b = pixels[ind], pixels[ind + w * h], pixels[ind + w * h * 2]
                img[y][x] = np.array([r, g, b])
                img_array_r[y][x] = r
                img_array_g[y][x] = g
                img_array_b[y][x] = b
                ind += 1

        ind = ind + h * w * 2
        #Image.fromarray(img, mode="RGB").save('{}/{}.jpg'.format(params["output"], frame))
        r_coeffs = map(lambda x: str(x), dct8(img_array_r)._compute_dct())
        g_coeffs = map(lambda x: str(x), dct8(img_array_g)._compute_dct())
        b_coeffs = map(lambda x: str(x), dct8(img_array_b)._compute_dct())

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

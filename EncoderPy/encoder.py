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

    pixels = np.fromfile(params["input"], dtype=np.int8)

    ind = 0
    count = 0
    while ind < len(pixels) and count < 40:
        count += 1
        img_array = np.zeros(shape=(h, w, channels), dtype=np.int8)
        for y in range(h):
            for x in range(w):
                r = pixels[ind]
                g = pixels[ind + w * h]
                b = pixels[ind + w * h * 2]
                img_array[y][x] = np.array([r, g, b])
                ind += 1
        ind = ind + h * w * 2
        Image.fromarray(img_array, mode="RGB").save('{}/{}.jpg'.format(params["output"], count))


if __name__ == "__main__":
    main()

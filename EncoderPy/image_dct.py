from scipy.fftpack import dct, idct
import numpy as np


def dct2(block):
    """
    Compute 2-D DCT on 8X8 block
    :param block: pixel block -- numpy array of shape (8, 8)
    :return: dct coefficients shape (8, 8)
    """
    return dct(dct(block, axis=0, norm='ortho'), axis=1, norm='ortho')


def idct2(block):
    """
    Compute 2-D IDCT on 8X8 block
    :param block: DCT coefficients -- numpy array of shape (8, 8)
    :return: original block of shape (8, 8)
    """
    return idct(idct(block, axis=0, norm='ortho'), axis=1, norm='ortho')


def test():
    og = [
        154, 123, 123, 123, 123, 123, 123, 136,
        192, 180, 136, 154, 154, 154, 136, 110,
        254, 198, 154, 154, 180, 154, 123, 123,
        239, 180, 136, 180, 180, 166, 123, 123,
        180, 154, 136, 167, 166, 149, 136, 136,
        128, 136, 123, 136, 154, 180, 198, 154,
        123, 105, 110, 149, 136, 136, 180, 166,
        110, 136, 123, 123, 123, 136, 154, 136]
    og_mat = np.array(og).reshape(8, 8)
    D = dct2(og_mat - 128)
    OG = idct2(D) + 128
    print(og_mat, OG)
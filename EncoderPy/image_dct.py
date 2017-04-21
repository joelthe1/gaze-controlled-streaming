from scipy.fftpack import dct, idct
import numpy as np


class dct8:
    def __init__(self, img, w=960, h=544):
        """
        :param img: single channel img of shape (h, w)
        :param w: width
        :param h: height
        """
        assert h%8 == 0 and w%8 == 0, "please provide compatible h, w"
        assert img.shape == (h, w), "wrong shape: provide a single channel image of shape (h, w)"
        # Subtract 128 from each pixel
        self.img = img.copy() - 128
        self.h = int(h)
        self.w = int(w)

    def _dct(self, block):
        """
        Compute 2-D DCT on 8X8 block
        :param block: pixel block -- numpy array of shape (8, 8)
        :return: dct coefficients shape (8, 8)
        """
        return dct(dct(block, axis=0, norm='ortho'), axis=1, norm='ortho')


    def _idct(self, block):
        """
        Compute 2-D IDCT on 8X8 block
        :param block: DCT coefficients -- numpy array of shape (8, 8)
        :return: original block of shape (8, 8)
        """
        return idct(idct(block, axis=0, norm='ortho'), axis=1, norm='ortho')

    def _compute_dct(self):
        computed_dct = np.zeros(shape=(self.h * self.w,), dtype=np.float32)

        k = 0
        for i in range(self.h // 8):
            for j in range(self.w // 8):
                block = self.img[i * 8: i * 8 + 8, j * 8: j * 8 + 8]
                dct_coeff_block = self._dct(block)
                # print block
                # print self._idct(dct_coeff_block)
                computed_dct[k : k + 64] = dct_coeff_block.flatten().astype(np.float32)
                k += 64

        return computed_dct



# def test():
#     og = [
#         154, 123, 123, 123, 123, 123, 123, 136,
#         192, 180, 136, 154, 154, 154, 136, 110,
#         254, 198, 154, 154, 180, 154, 123, 123,
#         239, 180, 136, 180, 180, 166, 123, 123,
#         180, 154, 136, 167, 166, 149, 136, 136,
#         128, 136, 123, 136, 154, 180, 198, 154,
#         123, 105, 110, 149, 136, 136, 180, 166,
#         110, 136, 123, 123, 123, 136, 154, 136]
#     og_mat = np.array(og).reshape(8, 8)
#     D = dct2(og_mat - 128)
#     OG = idct2(D) + 128
#     print(og_mat, OG)
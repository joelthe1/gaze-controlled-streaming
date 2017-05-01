from scipy.fftpack import dct, idct
import numpy as np


class dct8:
    def __init__(self, img, w=960, h=544):
        """
        :param img: single channel img of shape (h, w)
        :param w: width
        :param h: height
        """
        self.img = img.copy()
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
                computed_dct[k : k + 64] = dct_coeff_block.flatten().astype(np.float32)
                k += 64
        return computed_dct

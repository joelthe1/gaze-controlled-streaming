import numpy as np


class Brute:
    def __init__(self, k, macro_block_size, current_img, reference_img):
        assert current_img.shape == (960, 544), "current image shape is wrong"
        assert reference_img.shape == (960, 544), "reference image shape is wrong"
        self.k = k
        self.n = macro_block_size
        self.current_img = current_img
        self.reference_img = reference_img
        self.stride = 2

    def _SAD(self, a, b):
        assert a.shape == b.shape == (self.n, self.n), "_SAD operands: wrong shape"
        return np.sum(np.abs(a - b))

    def _motion_vector(self, cur_macro_block):
        """
        Method to find motion vector for a given block in current image from reference image.
        :return: (i, j) (left corner of the macro block)
        """
        x, y = cur_macro_block
        cur_macro_block = self.current_img[x: x + self.n, y: y + self.n]
        # full brute force search
        min_sad = np.float("inf")
        min_x, min_y = 0, 0
        for rx in range(max(x - self.k, 0), min(x + self.k, 960 - self.n), self.stride):
            for ry in range(max(y - self.k, 0), min(y + self.k, 544 - self.n), self.stride):
                ref_macro_block = self.reference_img[rx: rx + self.n, ry: ry + self.n]
                sad = self._SAD(ref_macro_block, cur_macro_block)
                if min_sad > sad:
                    min_sad, min_x, min_y = sad, rx, ry

        return np.array([min_x, min_y])

    def motionEstimation(self):
        """
        :return: numpy array of motion vectors for each block in the current_img
        """
        motion_vector = []
        for x in range(0, 960, self.n):
            for y in range(0, 544, self.n):
                motion_vector.append(self._motion_vector((x, y)))

        return np.array(motion_vector)

# cur_img = np.zeros(shape=(960, 544))
# cur_img[:16,:16] = 255
# ref_img = np.zeros(shape=(960,544))
# ref_img[32:48, 32:48] = 255
#
# test = Brute(40, 16, cur_img, ref_img)
# #print test._motion_vector((0, 0))
# print test.motionEstimation().shape

import cv2
import numpy as np
import time


class videoPlayer:
    def __init__(self, frames, h, w, ch, fps):
        self.height = h
        self.width = w
        self.channels = ch
        self.fps = fps
        self.frames = frames
        self.window = "videoPlayer"
        cv2.namedWindow(self.window)
        cv2.setMouseCallback(self.window, self._mouse_callback)
        self.mouse_x = 0
        self.mouse_y = 0

    def _mouse_callback(self, event, x, y, flags, param):
        self.mouse_x = x
        self.mouse_y = y

    def displayFrames(self):
        for frame in self.frames:
            time1 = time.time()
            cv2.circle(frame, (self.mouse_x, self.mouse_y), 100, (255, 0, 0), -1)
            cv2.imshow(self.window, frame)
            time2 = time.time()
            current_delay = max(int(1000 // self.fps - (time2 - time1) * 1000.0), 1)
            if cv2.waitKey(current_delay) == ord(' '):
                if cv2.waitKey(99999999) == ord(' '):
                    continue

    def close(self):
        cv2.destroyWindow(self.window)


def test():
    frames = np.load("frames_120_bgr.npy")
    player = videoPlayer(frames, 544, 960, 3, 24)
    player.displayFrames()
    player.close()
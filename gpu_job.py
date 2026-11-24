# Runs on Kaggle's free GPU: matrix multiplication, CPU vs GPU timing.
import time, math
import numpy as np
from numba import cuda

@cuda.jit
def matmul_kernel(A, B, C):
    row, col = cuda.grid(2)
    if row < C.shape[0] and col < C.shape[1]:
        tmp = 0.0
        for k in range(A.shape[1]):
            tmp += A[row, k] * B[k, col]
        C[row, col] = tmp

N = 1000
A = np.random.rand(N, N).astype(np.float32)
B = np.random.rand(N, N).astype(np.float32)

start = time.time()
C_cpu = np.dot(A, B)
print("CPU time:", time.time() - start)

dA, dB = cuda.to_device(A), cuda.to_device(B)
dC = cuda.device_array((N, N), dtype=np.float32)
blocks = (math.ceil(N / 16), math.ceil(N / 16))
matmul_kernel[blocks, (16, 16)](dA, dB, dC); cuda.synchronize()   # warm-up
start = time.time()
matmul_kernel[blocks, (16, 16)](dA, dB, dC); cuda.synchronize()
print("GPU time:", time.time() - start)
print("Results match:", np.allclose(C_cpu, dC.copy_to_host(), atol=1e-2))

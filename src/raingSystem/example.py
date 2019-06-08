import numpy as np
import matplotlib.pyplot as plt

# Fixing random state for reproducibility

# making a numpy array

# num of cases
N = 1000

arr = np.array([[100, 100, 100, 100, 100, 100, 100, 100, 100, 100]
               , [2000, 100, 100, 100, 100, 100, 100, 100, 100, 100]
               , [100, 100, 100, 100, 2000, 100, 100, 100, 100, 100]
               , [100, 100, 100, 100, 100, 100, 100, 100, 100, 2000]
               , [100, 200, 300, 400, 500, 600, 700, 800, 900, 1000]
               , [550, 550, 550, 550, 550, 550, 550, 550, 550, 550]
               , [2000, 100, 100, 100, 100, 100, 100, 100, 100, 100]])

res = np.zeros(shape=(7, 1))
x = np.arange(7)

for i in x:
    rating = 1500
    summitNum = 1
    for j in arr[i]:
        rating += (j/rating) * 1500 * (1/summitNum)
        print(j, rating)
    print(rating)
    print(i)
    res[i] = rating

plt.plot(x,res)
plt.show()


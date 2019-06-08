import numpy as np
import matplotlib.pyplot as plt
import random
# Fixing random state for reproducibility
np.random.seed(19680801)

# making a numpy array



for k in range(9):
    res = []
    for i in range(1, 50001):
        rating = 1500
        solvedProblem = random.randrange(1, 101)
        for j in range(solvedProblem):
            thisProb = random.randrange(1, 21) * 100
            summitNum = random.randrange(1, 10)
            rating += (thisProb/rating) * 1500 * (1/summitNum)
        res.append(rating)
    print(k)
    plt.subplot(3 , 3, k+1)
    n, bins, patches = plt.hist(res, 50, facecolor='g', alpha=0.75)

    plt.xlabel('Rating')
    plt.ylabel('People')
    plt.title('Rating Distribution')
    plt.text(10, 1900, "Case %d"%k)
    plt.axis([0, 15000, 0, 2100])
    plt.grid(True)

plt.show()

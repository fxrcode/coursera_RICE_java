def iterAvg():
    oldX = [0 for i in range(11)]
    oldX[10] = 1

    newX = [0 for i in range(11)]

    for step in range(0,200):
        for i in range(1,10):
            newX[i] = (oldX[i-1] + oldX[i+1] ) / 2.0
        # print(newX)
        oldX, newX = newX, oldX

    print(oldX)
    print(newX)

    X = [oldX[i] + newX[i] for i in range(len(oldX))]
    print(X)

def iterAvg_2():
    oldX = [0 for i in range(11)]
    oldX[10] = 1

    newX = [0 for i in range(11)]

    # for i in range(1,10):
    #     localNexX = newX
    #     localOldX = oldX
    #     for step in range(200):
    #         localNexX[i] = (localOldX[i-1] + localOldX[i+1] ) / 2.0
    #         # print(newX)
    #         localOldX, localNexX = localNexX, localOldX

    for i in range(1,10):
        for step in range(0,200):
            newX[i] = (oldX[i-1] + oldX[i+1] ) / 2.0
            # print(newX)
            oldX, newX = newX, oldX

    print(oldX)
    print(newX)

    X = [oldX[i] + newX[i] for i in range(len(oldX))]
    print(X)

iterAvg()

print("-----------------/n")
iterAvg_2()
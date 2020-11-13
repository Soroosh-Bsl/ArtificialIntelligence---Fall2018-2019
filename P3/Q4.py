import pandas as pd
import numpy as np

train = pd.read_csv('weather_train.csv')
train_x = train.iloc[:, :-2].values
train_y = train.iloc[:, -2].values

test = pd.read_csv('weather_test.csv')
test_x = test.iloc[:, :-2].values
test_y = test.iloc[:, -2].values


def linear_reg(x, y, num_steps, learning_rate, lambda_reg):
    x = np.concatenate((np.ones((x.shape[0], 1)), x), axis=1)
    w = np.zeros(x.shape[1])
    for step in range(num_steps):
        w = w - learning_rate * gradient_desc(x, y, lambda_reg, w)
    return w


def gradient_desc(x, y, lambda_reg, w):
    return -1/len(y) * np.dot(x.T, (y - np.dot(x, w))) + 2 * lambda_reg * w


def predict(x, w):
    last_x = x
    try:
        x = np.concatenate((np.ones((x.shape[0], 1)), x), axis=1)
        return np.dot(x, w)
    except ValueError:
        return np.dot(last_x, w)


def loss(predictions, labels, w, lambda_reg):
    return 1/2 * np.dot((labels - predictions).T, (labels - predictions)) + lambda_reg * np.dot(w.T, w)


# PART ONE
# weights = linear_reg(train_x, train_y, 200000, 0.00001, 0)
# weights = np.dot(np.dot(np.linalg.inv(np.dot(train_x.T, train_x)), train_x.T), train_y)
# print(loss(predict(test_x, weights), test_y, weights, 0))

# weights = np.array([0.89172184 , 0.66057563 ,-0.36785243,  0.75540489,  0.16991645,  0.01231751])
# weights = np.array([1.52934721,  0.83671929, -0.53904458,  1.27339914 , 0.11762974,  0.00963977])
# print(loss(predict(test_x, weights), test_y, weights, 0))


# PART TWO
min_lambda = 0
min_loss = float('inf')
train_x, validation_x = train_x[:(len(train_x)*4)//5], train_x[(len(train_x)*4)//5:]
train_y, validation_y = train_y[:(len(train_y)*4)//5], train_y[(len(train_y)*4)//5:]
weights = np.array([])
for lambda_reg in [0.001, 0.01, 0.1, 1, 10, 100, 1000]:
    weights = linear_reg(train_x, train_y, 200000, 0.00001, lambda_reg)
    now_loss = loss(predict(validation_x, weights), validation_y, weights, 0)
    if now_loss < min_loss:
        min_loss = now_loss
        min_lambda = lambda_reg
weights = np.array([0.00125349, 0.01954428, 0.01856586, 0.00080829, 0.01329356, 0.03634907])
print(weights)
print(loss(predict(test_x, weights), test_y, weights, min_lambda))
print("Best Lambda =", min_lambda)

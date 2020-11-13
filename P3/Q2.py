import numpy as np
import pandas as pd

with open("Train.txt") as f:
    train = f.readlines()
train = [x.strip() for x in train]
train = [x.split(',') for x in train]
train_x, train_y = [i for i in range(len(train))], [i for i in range(len(train))]
for i in range(len(train)):
    for j in range(len(train[i])):
        try:
            train[i][j] = int(train[i][j])
        except Exception:
            train[i][j] = 0
    train_x[i], train_y[i] = train[i][1:-2], (train[i][-1]//2)%2

with open("Test.txt") as f:
    test = f.readlines()
test = [x.strip() for x in test]
test = [x.split(',') for x in test]
test_x, test_y = [i for i in range(len(test))], [i for i in range(len(test))]
for i in range(len(test)):
    for j in range(len(test[i])):
        try:
            test[i][j] = int(test[i][j])
        except Exception:
            test[i][j] = 0
    test_x[i], test_y[i] = test[i][1:-2], (test[i][-1]//2)%2

train_x, train_y, test_x, test_y = np.array(train_x, dtype=np.int), np.array(train_y, dtype=np.int), np.array(test_x, dtype=np.int), np.array(test_y, dtype=np.int)


def sigmoid(weight_input):
    return 1 / (1 + np.exp(-weight_input))


def logistic_reg(input, label, num_of_steps, learning_rate):
    input = np.concatenate((np.ones((input.shape[0], 1)), input), axis=1)
    weights = np.random.normal(0, 1, input.shape[1])
    for step in range(num_of_steps):
        weight_input = np.dot(input, weights)
        predicted_labels = sigmoid(weight_input)
        gradient = np.dot(input.T, (predicted_labels - label)) / label.size
        weights -= learning_rate * gradient
    return weights


def predict(input, weights, threshold=0.5):
    return sigmoid(np.dot(input, weights)) >= threshold


def accuracy(predicted_labels, labels):
    counter = 0
    for i in range(len(predicted_labels)):
        if predicted_labels[i] == labels[i]:
            counter += 1
    return counter/len(predicted_labels)


def test_model(test_x, test_y, weights):
    test_x = np.concatenate((np.ones((test_x.shape[0], 1)), test_x), axis=1)
    predictions = []
    for x in test_x:
        predictions.append(predict(x, weights))
    return predictions, accuracy(predictions, test_y)


def confusion_matrix(real, prediction):
    matrix = [[0, 0], [0, 0]]
    for i in range(len(real)):
        matrix[real[i]][prediction[i]] += 1
    return matrix


weightOfFitness = logistic_reg(train_x, train_y, 100, 0.05)
predictions, accuracyOfModel = test_model(test_x, test_y, weightOfFitness)
print("Weights =", weightOfFitness)
print("Accuracy =", accuracyOfModel)
confusion_matrix_model = pd.DataFrame(confusion_matrix(test_y, predictions),
             columns=["Predicted Bad", "Predicted Good"],
             index=["Really Bad", "Really Good"])
print(confusion_matrix_model.to_string())
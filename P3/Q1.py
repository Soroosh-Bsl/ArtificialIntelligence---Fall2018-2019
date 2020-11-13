import pandas as pd
from scipy import io as scio
import math

train = scio.loadmat('mnist_train.mat')
train_x, train_y = train['X'], train['Y'][0]

test = scio.loadmat('mnist_test.mat')
test_x, test_y = test['X'], test['Y'][0]

repeats = [[[0 for i in range(17)] for i in range(64)] for i in range(10)]
label_reps = [0 for i in range(10)]
for i in range(len(train_x)):
    label_reps[int(train_y[i])] += 1
    for j in range(64):
        repeats[int(train_y[i])][j][int(train_x[i][j])] += 1

prob_each_label = [label_reps[i]/sum(label_reps) for i in range(10)]

predictions = []
right_num_pred = 0
wrong_num_pred = 0
for i in range(len(test_x)):
    best_prob = 0
    best_label = 0
    for j in range(10):
        prob = prob_each_label[j]
        for k in range(64):
            sum_by_pixel = 0
            prob *= repeats[j][k][int(test_x[i][k])]/sum(repeats[j][k]) if sum(repeats[j][k]) != 0 else 0
        if prob > best_prob:
            best_prob = prob
            best_label = j
    predictions.append(best_label)
    if predictions[i] == test_y[i]:
        right_num_pred += 1
    else:
        wrong_num_pred += 1


def confusion_matrix(predictions, labels):
    confusion = [[0 for i in range(10)] for i in range(10)]
    for i in range(len(predictions)):
        confusion[labels[i]][predictions[i]] += 1
    return confusion


confusion_matrix_model = pd.DataFrame(confusion_matrix(predictions, test_y),
             columns=["Predicted 0", "Predicted 1", "Predicted 2", "Predicted 3", "Predicted 4", "Predicted 5", "Predicted 6", "Predicted 7", "Predicted 8", "Predicted 9"],
             index=["Real 0", "Real 1", "Real 2", "Real 3", "Real 4", "Real 5", "Real 6", "Real 7", "Real 8", "Real 9"])

print(confusion_matrix_model.to_string())
print("Right Preds =", right_num_pred, "Wrong Preds =", wrong_num_pred, "Acc =", right_num_pred/(wrong_num_pred + right_num_pred))

train_x, validation_x = train_x[:(len(train_x)*4)//5], train_x[(len(train_x)*4)//5:]
train_y, validation_y = train_y[:(len(train_y)*4)//5], train_y[(len(train_y)*4)//5:]


repeats = [[[0 for i in range(17)] for i in range(64)] for i in range(10)]
label_reps = [0 for i in range(10)]
for i in range(len(train_x)):
    label_reps[int(train_y[i])] += 1
    for j in range(64):
        repeats[int(train_y[i])][j][int(train_x[i][j])] += 1

prob_each_label = [label_reps[i]/sum(label_reps) for i in range(10)]

best_K = 0
predict_of_Ks = []
best_accuracy = 0
for K in [1, 2, 3, 4, 5]:
    accuracy = []
    predictions = []
    right_num_pred = 0
    wrong_num_pred = 0
    for i in range(len(validation_x)):
        best_prob = 0
        best_label = 0
        for j in range(10):
            prob = prob_each_label[j]
            for k in range(64):
                prob *= (repeats[j][k][int(validation_x[i][k])] + 1)/(sum(repeats[j][k]) + K)
            if prob > best_prob:
                best_prob = prob
                best_label = j
        predictions.append(best_label)
        if predictions[i] == validation_y[i]:
            right_num_pred += 1
        else:
            wrong_num_pred += 1
    accuracy.append(right_num_pred/(wrong_num_pred + right_num_pred))
    predict_of_Ks.append(predictions)
    best_accuracy = 0
    for i in range(len(accuracy)):
        if accuracy[i] > best_accuracy:
            best_accuracy = accuracy[i]
            best_K = i+1

predictions = []
for i in range(len(test_x)):
    best_prob = 0
    best_label = 0
    for j in range(10):
        prob = prob_each_label[j]
        for k in range(64):
            prob *= (repeats[j][k][int(test_x[i][k])] + 1) / (sum(repeats[j][k]) + best_K)
        if prob > best_prob:
            best_prob = prob
            best_label = j
    predictions.append(best_label)
    if predictions[i] == test_y[i]:
        right_num_pred += 1
    else:
        wrong_num_pred += 1

confusion_matrix_model = pd.DataFrame(confusion_matrix(predictions, test_y),
             columns=["Predicted 0", "Predicted 1", "Predicted 2", "Predicted 3", "Predicted 4", "Predicted 5", "Predicted 6", "Predicted 7", "Predicted 8", "Predicted 9"],
             index=["Real 0", "Real 1", "Real 2", "Real 3", "Real 4", "Real 5", "Real 6", "Real 7", "Real 8", "Real 9"])

print(confusion_matrix_model.to_string())
print("Acc =", right_num_pred/(right_num_pred+wrong_num_pred), "Best K =", best_K)

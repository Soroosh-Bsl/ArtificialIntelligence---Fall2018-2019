from sklearn import tree
from sklearn.metrics import accuracy_score, confusion_matrix
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

model = tree.DecisionTreeClassifier()
model.fit(train_x, train_y)
predicted_labels = model.predict(test_x)
print("Accuracy = ", accuracy_score(test_y, predicted_labels))
confusion_matrix_model = pd.DataFrame(confusion_matrix(test_y, predicted_labels),
             columns=["Predicted Bad", "Predicted Good"],
             index=["Really Bad", "Really Good"])
print(confusion_matrix_model.to_string())
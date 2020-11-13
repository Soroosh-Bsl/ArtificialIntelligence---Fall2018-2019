n, m = map(int, input().split())

k = int(input())

adjList = [[] for i in range(n)]


for i in range(k):
    a, b = map(int, input().split())
    adjList[a].append(b)

knowledge = [[] for i in range(n)]

for i in range(m):
    a = list(map(int, input().split()))
    for j, k in enumerate(a):
        if k > 80:
            knowledge[j].append(i)


marked = [False for i in range(n)]
topologicalSort = list()


def dfs(node):
    if marked[node]:
        return
    marked[node] = True
    # topologicalSort.append(node)
    for u in adjList[node]:
        dfs(u)
    topologicalSort.insert(0, node)
    return True


def DFS():
    for i in range(n):
        if not marked[i]:
            dfs(i)

DFS()

# print(knowledge)
# print(topologicalSort)

for i in range(n - 1, 0, -1):
    if len(knowledge[topologicalSort[i]]) == 1 \
            and knowledge[topologicalSort[i]][0] in knowledge[topologicalSort[i - 1]]:
        knowledge[topologicalSort[i - 1]].remove(knowledge[topologicalSort[i]][0])

ok = True
answer = [None for i in range(n)]
for j in range(n):
    if len(knowledge[topologicalSort[j]]) <= 0:
        print("no assignment")
        ok = False
        break
    answer[topologicalSort[j]] = knowledge[topologicalSort[j]][0]
    for i in adjList[topologicalSort[j]]:
        if knowledge[topologicalSort[j]][0] in knowledge[i]:
            knowledge[i].remove(knowledge[topologicalSort[j]][0])

if ok:
    for i, j in enumerate(answer):
        print(str(i) + " " + str(j))
n, m = map(int, input().split())
k = int(input())

pishniazi = [[] for i in range(n)]
pasniazi = [[] for i in range(n)]

visited = [False for i in range(n)]
stack = []

for i in range(k):
    tmp, tmp2 = map(int, input().split())
    pishniazi[tmp].append(tmp2)
    pasniazi[tmp2].append(tmp)

ostad = []
for i in range(m):
    ostad.append(list(map(int, input().split())))


def topological_rec(node):
    global pishniazi, visited, stack
    visited[node] = True
    for i in pishniazi[node]:
        if not visited[i]:
            topological_rec(i)
    stack.append(node)


def topological_sort():
    global pishniazi, visited, stack
    for i in range(n):
        if not visited[i]:
            topological_rec(i)
    stack.reverse()


topological_sort()
topological_order = stack
domain = [[] for i in range(n)]
result = [-1 for i in range(n)]


def unary():
    global ostad, topological_order
    for i in range(len(topological_order)):
        for j in range(len(ostad)):
            if ostad[j][topological_order[i]] > 80:
                domain[topological_order[i]].append(j)
        if len(domain[topological_order[i]]) == 0:
            return False
    return True


def binary():
    global domain, topological_order
    for i in range(n-1, 0, -1):
        for k in pasniazi[topological_order[i]]:
            if len(domain[topological_order[i]]) == 1 and domain[topological_order[i]][0] in domain[k]:
                domain[k].remove(domain[topological_order[i]][0])
                if len(domain[k]) == 0:
                    return False
    if len(domain[topological_order[0]]) == 0:
        return False
    return True


def resulting():
    global domain, topological_order, result
    for i in range(n):
        result[topological_order[i]] = domain[topological_order[i]][0]
        for j in pishniazi[topological_order[i]]:
            if domain[topological_order[i]][0] in domain[j]:
                domain[j].remove(domain[topological_order[i]][0])
                if len(domain[j]) == 0:
                    return False
    return True


no = False
is_unary = unary()
is_binary = binary()
for i in domain:
    if len(i) == 0:
        no = True
if is_unary and is_binary and not no:
    if resulting():
            for i in range(len(result)):
                print(i, result[i])
    else:
        print("no assignment")
else:
    print("no assignment")

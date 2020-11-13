n = int(input())
start_x, start_y = map(int, input().split())
start_x, start_y = start_x-1, start_y-1
start = (start_x, start_y)
goal_x, goal_y = map(int, input().split())
goal_x, goal_y = goal_x-1, goal_y-1
goal = (goal_x, goal_y)
speed, strength, size, height, adaptability = map(int, input().split())
zamin = {}
level = {}

for y in range(n):
    tmp = list(input().split())
    for x in range(n):
        zamin[(x, y)] = tmp[x]
        if zamin[(x, y)] != 'e':
            level[(x, y)] = int(zamin[(x, y)][1])
            zamin[(x, y)] = zamin[(x, y)][0]


class Graph:
    def __init__(self, n):
        self.n = n

    def if_passable(self, node):
        (x, y) = node
        bound_check = True if(0 <= x < self.n and 0 <= y < self.n) else False
        level_check = True

        if bound_check:
            if zamin[node] == 'm' and level[node] > speed:
                level_check = False
            elif zamin[node] == 'M' and level[node] > strength:
                level_check = False
            elif zamin[node] == 'h' and level[node] > size:
                level_check = False
            elif zamin[node] == 't' and level[node] > height:
                level_check = False
            elif zamin[node] == 'd' and level[node] > adaptability:
                level_check = False

        if bound_check and level_check:
            return True
        else:
            return False

    def neighbours(self, node):
        (x, y) = node
        neighbours = [(x+1, y), (x-1, y), (x, y+1), (x, y-1)]
        true_neighbours = []
        for tmp in neighbours:
            if self.if_passable(tmp):
                true_neighbours.append(tmp)
        return true_neighbours


class PriorityQueue:
    def __init__(self):
        self.elements = []

    def empty(self):
        return len(self.elements) == 0

    def enter(self, node):
        self.elements.append(node)

    def exit(self):
        min_priority_indx = 0
        for i in range(len(self.elements)):
            (_, prior_min) = self.elements[min_priority_indx]
            (_, prior_i) = self.elements[i]
            if prior_min >= prior_i:
                min_priority_indx = i
        min_val, _ = self.elements[min_priority_indx]
        del self.elements[min_priority_indx]
        return min_val, _

    def show_all(self):
        for any in self.elements:
            node, f = any
            print_x, print_y = node
            print_x, print_y = print_x + 1, print_y + 1
            print_it = (print_x, print_y)
            print("#", print_it, "F =", f)


def manhattan(node):
    global goal
    (x_node, y_node) = node
    (x_goal, y_goal) = goal
    return abs(x_node-x_goal) + abs(y_node-y_goal)

k = 0
dfs_result = []
visited = []


def dfs(graph, node, depth):
    global k, visited, goal
    visited.append(node)

    neighbours = graph.neighbours(node)
    for x in neighbours:
        if x == goal:
            dfs_result.append((k + 1, x))
        if x not in visited:
            if k < depth:
                visited.append(x)
                k += 1
                dfs(graph, x, depth)
                k -= 1
            else:
                dfs_result.append((manhattan(x) + k + 1, x))


def h(graph, node, came):
    global visited, k, goal, dfs_result
    depth = 5
    dfs_result = []
    visited = [came]
    k = 0
    if node == goal:
        return 0
    dfs(graph, node, depth)
    results = sorted(dfs_result)
    if len(results) == 0:
        h_res = 1000000
    else:
        h_res, _ = results[0]
    return h_res

def traversal(graph, starting_node, goal):
    visiting_queue = PriorityQueue()
    path_to_this_node = {}
    cost_to_this_node = {}
    cost_to_this_node[starting_node] = 0
    visiting_queue.enter((starting_node, 0 + h(graph, starting_node, None)))

    while not visiting_queue.empty():
        visiting_queue.show_all()
        node, prior = visiting_queue.exit()
        neighbours = graph.neighbours(node)

        print_x, print_y = node
        print_x, print_y = print_x + 1, print_y + 1
        print_it = (print_x, print_y)
        print("EXPANDING NODE #", print_it, "F =", prior, "with g =", prior - h(graph, node, path_to_this_node[node] if node != starting_node else None), "and h =", h(graph, node, path_to_this_node[node]) if node != starting_node else None)
        if node == goal:
            break

        now_cost = cost_to_this_node[node] + 1
        for x in neighbours:
            if x not in cost_to_this_node or now_cost < cost_to_this_node[x]:
                print_x, print_y = x
                print_x, print_y = print_x+1, print_y+1
                print_it = (print_x, print_y)
                print("INSERTED NODE #", print_it, "F =", now_cost + h(graph, x, node), "with g =", now_cost, "and h =", h(graph, x, node))
                cost_to_this_node[x] = now_cost
                path_to_this_node[x] = node
                visiting_queue.enter((x, now_cost + h(graph, x, node)))

    current = goal
    whole_path = []
    try:
        while current != start:
            whole_path.append(current)
            current = path_to_this_node[current]
        whole_path.append(start)
        whole_path.reverse()
        return whole_path
    except:
        return []


grid = Graph(n)
path = traversal(grid, start, goal)
if len(path) != 0:
    for x in path:
        print_x, print_y = x
        print_x, print_y = print_x + 1, print_y + 1
        print_it = (print_x, print_y)
        print(print_it)
else:
    print("GOAL NOT REACHABLE")

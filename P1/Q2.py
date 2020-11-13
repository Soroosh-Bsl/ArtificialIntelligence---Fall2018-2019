import random
import time
import numpy

n = int(input())
m = int(input())
starting_position_x, starting_position_y = map(int, input().split())
starting_position_x, starting_position_y = starting_position_x - 1, starting_position_y - 1
grid = [["e" for x in range(n)] for i in range(n)]
tmp = list(map(str, input()[2:-2].split(", ")))
for y in range(1, n):
    x_s = list(map(str, input()[1:-2].split(", ")))
    for x in range(n):
        grid[x][y] = x_s[x]


def condition(x, y, copy_grid):
    if x < 0 or x >= n or y < 0 or y >= n or copy_grid[x][y] == "w":
        return 0
    if copy_grid[x][y] == "s":
        return 1
    if copy_grid[x][y] == "a":
        return 2
    return 3


codes = list(numpy.random.permutation(1024))


def hash_situation(x, y, copy_grid):
    global codes
    left = condition(x-1, y, copy_grid)
    right = condition(x+1, y, copy_grid)
    up = condition(x, y+1, copy_grid)
    down = condition(x, y-1, copy_grid)
    now = condition(x, y, copy_grid)
    code = left*(4**4) + up*(4**3) + right*(4**2) + down*(4) + now
    return codes[code]


def fitness(chromosome, show):
    global m, n, starting_position_x, starting_position_y, grid
    break_commit = False
    if show:
        print(starting_position_x, starting_position_y)
    position_x = starting_position_x
    position_y = starting_position_y
    ammo = 0
    copy_grid = [["e" for x in range(n)] for i in range(n)]
    for x in range(n):
        for y in range(n):
            copy_grid[x][y] = grid[x][y]
    periods_passed = 0
    for i in range(m):
        index = hash_situation(position_x, position_y, copy_grid)
        if show:
            print(index, chromosome[index])
        if chromosome[index] == "U":
            position_y = position_y + 1 if position_y + 1 < n and copy_grid[position_x][position_y+1] != "w" else position_y
        elif chromosome[index] == "D":
            position_y = position_y - 1 if position_y - 1 >= 0 and copy_grid[position_x][position_y-1] != "w" else position_y
        elif chromosome[index] == "R":
            position_x = position_x + 1 if position_x + 1 < n and copy_grid[position_x+1][position_y] != "w" else position_x
        elif chromosome[index] == "L":
            position_x = position_x - 1 if position_x - 1 >= 0 and copy_grid[position_x-1][position_y] != "w" else position_x
        elif chromosome[index] == "O":
            x_range = [position_x]
            y_range = [position_y]
            if position_x - 1 > 0 and copy_grid[position_x - 1][position_y] != "w":
                x_range.append(position_x-1)
            if position_x + 1 < n and copy_grid[position_x+1][position_y] != "w":
                x_range.append(position_x+1)
            if position_y - 1 > 0 and copy_grid[position_x][position_y - 1] != "w":
                y_range.append(position_y-1)
            if position_y + 1 < n and copy_grid[position_x][position_y + 1] != "w":
                y_range.append(position_y+1)
            position_x, position_y = random.choice(x_range), random.choice(y_range)
        # else:
        #     position_x, position_y = random.randint(0, n-1), random.randint(0, n-1)
        if show:
            print("-------------")
            print(position_x, position_y, grid[position_x][position_y])

        if copy_grid[position_x][position_y] == "a":
            ammo += 1
            if show:
                print("AMMO INC.", ammo)
            copy_grid[position_x][position_y] = "e"
        elif copy_grid[position_x][position_y] == "s":
            ammo -= 1
            if show:
                print("AMMO DEC.", ammo)
            copy_grid[position_x][position_y] = "e"
            if ammo < 0:
                ammo = 0
                if show:
                    print("DEATH")
                break_commit = True
                periods_passed = i
                break
        periods_passed = i
    if not break_commit:
        periods_passed += 1
    if show:
        print("#######", ammo, periods_passed, ammo+periods_passed)
    return ammo + periods_passed


def selection(population, num_of_parents):
    parents = []
    tmp_pop = []
    for chromosome in population[len(population)-num_of_parents*2:]:#TODO
        tmp_pop.append(chromosome)
    for i in range(num_of_parents):
        parents.append(random.choice(tmp_pop))
        tmp_pop.remove(parents[-1])
    return parents


def cross_over(parents, num_of_children):
    children = []
    for i in range(num_of_children):
        cross_over_point = random.randint(0, 1024-1)
        first_parent = random.choice(parents)
        tmp_pars = []
        for p in parents:
            tmp_pars.append(p)
        tmp_pars.remove(first_parent)
        second_parent = random.choice(tmp_pars)
        child = first_parent[:cross_over_point] + second_parent[cross_over_point:]
        children.append(child)
    return children


def mutation(new_children, num_of_mutation):
    for i in range(len(new_children)):
        chance = random.randint(0, 5-1)
        for j in range(num_of_mutation):
            if chance < 3: #TODO
                continue
            mutation_index = random.randint(0, 1024-1)
            rand_num = random.randint(0, 6-1)
            if rand_num == 0:
                rand_char = "R"
            elif rand_num == 1:
                rand_char = "L"
            elif rand_num == 2:
                rand_char = "U"
            elif rand_num == 3:
                rand_char = "D"
            elif rand_num == 4:
                rand_char = "S"
            else:
                rand_char = "O"
            new_children[i][mutation_index] = rand_char
    return new_children


def initialize_population(number_of_population):
    population = []
    for i in range(number_of_population):
        chromosome = []
        for j in range(1024):
            rand_num = random.randint(0, 6-1)
            if rand_num == 0:
                rand_char = "R"
            elif rand_num == 1:
                rand_char = "L"
            elif rand_num == 2:
                rand_char = "U"
            elif rand_num == 3:
                rand_char = "D"
            elif rand_num == 4:
                rand_char = "S"
            else:
                rand_char = "O"
            chromosome.append(rand_char)
        population.append(chromosome)
    return population


def insert_chromosomes_and_fit_size(population, chromosomes, fit):
    for chromosome in chromosomes:
        fit.append((fitness(chromosome, False)+fitness(chromosome, False)+fitness(chromosome, False))//3)
        population.append(chromosome)
    fit, population = zip(*sorted(zip(fit, population)))
    fit, population = list(fit), list(population)
    population = population[len(chromosomes):]
    fit = fit[len(chromosomes):]
    return fit, population


def run(finish_time, number_of_population):
    global grid
    population = initialize_population(number_of_population)
    fit = []
    for chromosome in population:
        fit.append((fitness(chromosome, False)+fitness(chromosome, False)+fitness(chromosome, False))//3)
    fit, population = zip(*sorted(zip(fit, population)))
    fit, population = list(fit), list(population)
    t = time.time()
    finish_time += t
    best_fitness = -1
    best_chromosome = []
    num_of_children = 30 #TODO
    num_of_parents = 10 #TODO
    while t < finish_time:
        parents = selection(population, num_of_parents)
        new_children = cross_over(parents, num_of_children)
        new_children = mutation(new_children, num_of_mutation=256) #TODO
        fit, population = insert_chromosomes_and_fit_size(population, new_children, fit)
        if best_fitness < fit[-1]:
            best_chromosome = population[-1]
            best_fitness = fit[-1]
        print("Best Score Until Now =", best_fitness)
        # print("Best Chromosome Until Now =", best_chromosome)
        t = time.time()
    return best_chromosome, best_fitness


num_of_population = 100 #TODO
rec_time = 1 * 60 #TODO
best_solution, score = run(rec_time, num_of_population)
print("SCORE =", score)
print(best_solution)
print(str(codes))
print(fitness(best_solution, True))

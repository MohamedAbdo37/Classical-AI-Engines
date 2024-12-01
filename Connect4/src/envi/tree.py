def tree_node(state):
    X=''
    for row in range(6) :
        for col in range(7) :
            slot = state.slot(5-row , col )
            if slot =='e' :
                X = X +' | ' + ' '
            else :
                X = X +' | ' + slot

        X = X +' |\n'
    
    return X 

def node_children(state , queue) :
    for col in range(7):
        row = int(state.cols.decode("ASCII")[col])
        if  row <6 :
            child = state.copy()
            if state.turn == 1 :
                child.set_slot(row , col , 'x')
            elif state.turn == 2:
                child.set_slot(row , col , 'o')

            child.increase_col(col)
            child.depth = child.depth+1
            state.children.append(child)
            if state.turn ==1 :
                child.turn =2
            else :
                child.turn = 1
            queue.append(child)
    return queue


def extract_tree(initial_state , k):
    queue = []
    queue.append(initial_state)
    while len(queue)!=0:
        state = queue.pop()
        if state.depth <k :
            queue = node_children(state , queue)


from envi_state import EnviState
import time
import graphviz  # doctest: +NO_EXE


initial_state = EnviState()
extract_tree(initial_state , 3)


dot = graphviz.Digraph(comment='The Round Table')
dot  #doctest: +ELLIPSIS
initial_state.node_name = 'A'
dot.node(initial_state.node_name , tree_node(initial_state).lower() , shape = 'box')  # doctest: +NO_EXE
queue = [] 
queue.append(initial_state)
num = 0
while len(queue)!=0 :
    state = queue.pop()
    for i in range(len(state.children)):
        state.children[i].node_name = 'a' + str(num)
        dot.node(state.children[i].node_name, tree_node(state.children[i]) , shape = 'box')
        dot.edge(state.node_name ,state.children[i].node_name)
        num = num+1
        queue.append(state.children[i])


dot.render('D://round-table', format='svg' , view=True)  # doctest: +SKIP


'''

#print(tree_node(initial_state))

for i in range(len(initial_state.children)):
    node_name = 'a' + str(i)
    print(node_name)
    dot.node(node_name, tree_node(initial_state.children[i]) , shape = 'box')
    dot.edge('A' , node_name)

k = 0 
for i in range(len(initial_state.children)) :
    node_children(initial_state.children[i])
    for j in range (len(initial_state.children[i].children)) :
        node_name = 'b'+str(k)
        dot.node(node_name, tree_node(initial_state.children[i].children[j]) , shape = 'box')
        print(node_name)
        dot.edge('a'+str(i) , node_name)
        k = k+1

dot.render('D://round-table', format='svg' , view=True)  # doctest: +SKIP

'''
'''

dot.node('B', 'Sir Bedevere the Wise')
dot.node('C', 'Sir Lancelot the Brave')
dot.node('D', 'Sir Lancelot the Brave')
dot.node('E', 'Sir Lancelot the Brave')


dot.edges(['AB'])

print(dot.source)  # doctest: +NORMALIZE_WHITESPACE +NO_EXE



dot.render('D://round-table', format='svg' , view=True)  # doctest: +SKIP

'''
from src.envi.envi_state import EnviState 
import graphviz  
count = 1

class tree_generation :


    # generating children of a node
    def node_children(state) :
        for col in range(7):
            row = state.find_row(col)

            if  row < 6 :
                child = state.copy()
                child.children.clear() 

                if state.turn == 1 :
                    child.play_at('x' , col)
                elif state.turn == 2:
                    child.play_at('o' , col)

                child.depth = state.depth+1
                if state.turn ==1 :
                    child.turn =2
                else :
                    child.turn = 1
                state.children.append(child)

    
    # formating for printing node in tree
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
        X = X +  '\nValue = ' + str(state.utility)
        return X 

    
    # generating search tree in valid format for printing
    def generating_tree (initial_state):
        dot = graphviz.Digraph()
        dot  

        # generating root
        initial_state.node_name = 'root'
        dot.node(initial_state.node_name , tree_generation.tree_node(initial_state))

        # printing nodes level by level
        queue = [] 
        queue.append(initial_state)
        num = 0
        while len(queue)!=0 :
            state = queue.pop()
            for i in range(len(state.children)):
                state.children[i].node_name = 'node' + str(num)
                dot.node(state.children[i].node_name, tree_generation.tree_node(state.children[i]))
                dot.edge(state.node_name ,state.children[i].node_name)
                num = num+1
                queue.append(state.children[i])
        global count
        print(count)
        # opening file
        dot.render('D://search tree' + str(count), format='svg')    
        
        count = count +1   
        return 'D://search tree' + str(count) + '.svg'
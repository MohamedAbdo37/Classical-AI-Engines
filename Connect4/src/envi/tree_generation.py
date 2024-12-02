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

# --------------------------------------------------------------------------------
   
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

# ----------------------------------------------------------------------------------
 
    # generating search tree in valid format for printing
    def generating_tree (initial_state):
        dot = graphviz.Digraph()
        dot  

        # generating root
        initial_state.node_name = 'root'
        dot.node(initial_state.node_name , tree_generation.tree_node(initial_state) , shape = 'triangle' , color = 'purple' , style = 'filled')

        # printing nodes level by level
        queue = [] 
        queue.append(initial_state)
        num = 0
        while len(queue)!=0 :
            state = queue.pop()
            for i in range(len(state.children)):
                state.children[i].node_name = 'node' + str(num)
                if(state.depth %2 == 0) :
                    dot.node(state.children[i].node_name, tree_generation.tree_node(state.children[i]) , shape = 'invtriangle' , color = 'orange' , style = 'filled')
                else :
                    dot.node(state.children[i].node_name, tree_generation.tree_node(state.children[i]) , shape = 'triangle' , color = 'purple' , style = 'filled')

                dot.edge(state.node_name ,state.children[i].node_name)
                num = num+1
                queue.append(state.children[i])


        global count
        # opening file
        dot.render('D://search tree' + str(count), format='svg')    
        count = count +1   

        return 'D://search tree' + str(count-1) + '.svg'



    def excepted_minmax_tree(initial_state) :
        dot = graphviz.Digraph()
        dot  

        # generating root
        initial_state.node_name = 'root'
        dot.node(initial_state.node_name , tree_generation.tree_node(initial_state) ,shape = 'triangle' , color = 'purple' , style = 'filled')

        # printing nodes level by level
        queue = [] 
        queue.append(initial_state)
        num = 0
        chance_num = 0 

        while len(queue)!=0 :
            state = queue.pop()

            if state.children == [] :
                continue 
            
            for col in range (7) :

                if state.children[col] != None :

                    if state.depth % 2 == 0 :
                        shape = 'invtriangle' 
                        color = 'orange'

                    else :
                        shape = 'triangle' 
                        color = 'purple' 

                    
                    dot.node('chance' + str(chance_num), '' , shape = 'circle' , color = 'yellow' , style = 'filled')
                    dot.edge(state.node_name , 'chance' + str(chance_num))
                        
                    if (state.chance_nodes[col]) == 3 :

                        state.children[col-1].node_name = 'node' + str(num)
                        dot.node(state.children[col-1].node_name, tree_generation.tree_node(state.children[col-1]) , shape = shape , color = color , style = 'filled')
                        num = num+1
                        dot.edge('chance' + str(chance_num) , state.children[col-1].node_name , '.2')


                        state.children[col].node_name = 'node' + str(num)
                        dot.node(state.children[col].node_name, tree_generation.tree_node(state.children[col]) ,  shape = shape , color = color , style = 'filled')
                        num = num+1
                        dot.edge('chance' + str(chance_num) , state.children[col].node_name , '.6')

                        state.children[col+1].node_name = 'node' + str(num)
                        dot.node(state.children[col+1].node_name, tree_generation.tree_node(state.children[col+1]) ,  shape = shape , color = color , style = 'filled')
                        num = num+1
                        dot.edge('chance' + str(chance_num) , state.children[col+1].node_name , '.2')

                        queue.append(state.children[col-1].copy())
                        queue.append(state.children[col].copy())
                        queue.append(state.children[col+1].copy())


                    elif (state.chance_nodes[col]) == 2 :
                        if col ==0 :
                            state.children[col].node_name = 'node' + str(num)
                            dot.node(state.children[col].node_name, tree_generation.tree_node(state.children[col]) , shape = shape , color = color , style = 'filled')
                            num = num+1
                            dot.edge('chance' + str(chance_num) , state.children[col].node_name , '.6')

                            state.children[col+1].node_name = 'node' + str(num)
                            dot.node(state.children[col+1].node_name, tree_generation.tree_node(state.children[col+1]) ,  shape = shape , color = color , style = 'filled')
                            num = num+1
                            dot.edge('chance' + str(chance_num) , state.children[col+1].node_name , '.4')

                            queue.append(state.children[col].copy())
                            queue.append(state.children[col+1].copy())


                        elif col == 6 :
                            state.children[col-1].node_name = 'node' + str(num)
                            dot.node(state.children[col-1].node_name, tree_generation.tree_node(state.children[col-1]) ,  shape = shape , color = color , style = 'filled')
                            num = num+1
                            dot.edge('chance' + str(chance_num) , state.children[col-1].node_name , '.4')


                            state.children[col].node_name = 'node' + str(num)
                            dot.node(state.children[col].node_name, tree_generation.tree_node(state.children[col]) ,  shape = shape , color = color , style = 'filled')
                            num = num+1
                            dot.edge('chance' + str(chance_num) , state.children[col].node_name , '.6')

                            queue.append(state.children[col-1].copy())
                            queue.append(state.children[col].copy())


                        else :
                            if state.children[col-1] == None :
                                state.children[col].node_name = 'node' + str(num)
                                dot.node(state.children[col].node_name, tree_generation.tree_node(state.children[col]) ,  shape = shape , color = color , style = 'filled')
                                num = num+1
                                dot.edge('chance' + str(chance_num) , state.children[col].node_name , '.6')

                                state.children[col+1].node_name = 'node' + str(num)
                                dot.node(state.children[col+1].node_name, tree_generation.tree_node(state.children[col+1]) ,  shape = shape , color = color , style = 'filled')
                                num = num+1
                                dot.edge('chance' + str(chance_num) , state.children[col+1].node_name , '.4')

                                queue.append(state.children[col].copy())
                                queue.append(state.children[col+1].copy())

                            else :
                                state.children[col-1].node_name = 'node' + str(num)
                                dot.node(state.children[col-1].node_name, tree_generation.tree_node(state.children[col-1]) ,  shape = shape , color = color , style = 'filled')
                                num = num+1
                                dot.edge('chance' + str(chance_num) , state.children[col-1].node_name , '.4')

                                state.children[col].node_name = 'node' + str(num)
                                dot.node(state.children[col].node_name, tree_generation.tree_node(state.children[col]) ,  shape = shape , color = color , style = 'filled')
                                num = num+1
                                dot.edge('chance' + str(chance_num) , state.children[col].node_name , '.6')

                                queue.append(state.children[col-1].copy())
                                queue.append(state.children[col].copy())

                    else :
                        state.children[col].node_name = 'node' + str(num)
                        dot.node(state.children[col].node_name, tree_generation.tree_node(state.children[col]) ,  shape = shape , color = color , style = 'filled')
                        num = num+1
                        dot.edge('chance' + str(chance_num) , state.children[col].node_name , '1')

                        queue.append(state.children[col].copy())
                    
                    chance_num = chance_num + 1

        global count
        # opening file
        dot.render('D://search tree ' + str(count), format='svg')    
        count = count +1   
        return 'D://search tree ' + str(count-1) + '.svg'

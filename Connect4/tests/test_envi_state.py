from turtle import st
from src.envi.envi_state import EnviState 

def test_envi_state():
    state = EnviState()
    print(str(state))
    assert str(state) == '[[\'e\', \'e\', \'e\', \'e\', \'e\', \'e\', \'e\'], [\'e\', \'e\', \'e\', \'e\', \'e\', \'e\', \'e\'], [\'e\', \'e\', \'e\', \'e\', \'e\', \'e\', \'e\'], [\'e\', \'e\', \'e\', \'e\', \'e\', \'e\', \'e\'], [\'e\', \'e\', \'e\', \'e\', \'e\', \'e\', \'e\'], [\'e\', \'e\', \'e\', \'e\', \'e\', \'e\', \'e\']]'

def test_envi_state_copy():
    state = EnviState()
    state_copy = state.copy()
    assert str(state) == str(state_copy)

    state.set_slot(0, 0, 'x')
    assert str(state) != str(state_copy)

    state_copy.set_slot(0, 0, 'x')
    assert str(state) == str(state_copy)
    
    print(str(state_copy))
    state_copy.set_slot(0, 0, 'o')
    print(str(state_copy))
    print(str(state))
    assert str(state) != str(state_copy)
    

    state.set_slot(0, 0, 'o')
    assert str(state) == str(state_copy)
    
    print(str(state))

    state.set_slot(5, 5, 'x')
    assert str(state) != str(state_copy)
    
    state_copy.set_slot(5, 5, 'x')
    assert str(state) == str(state_copy)
    
    print(str(state))

    state_copy.set_slot(5, 5, 'o')
    assert str(state) != str(state_copy)

    state.set_slot(5, 5, 'o')
    assert str(state) == str(state_copy)
    
    print(str(state))

def test_envi_state_slot():
    state = EnviState()
    assert state.slot(0, 0) == 'e'

def test_envi_state_set_slot():
    state = EnviState()
    state.set_slot(0, 0, 'x')
    print(str(state))
    assert state.slot(0, 0) == 'x'
    assert str(state) == '[[\'e\', \'e\', \'e\', \'e\', \'e\', \'e\', \'e\'], [\'e\', \'e\', \'e\', \'e\', \'e\', \'e\', \'e\'], [\'e\', \'e\', \'e\', \'e\', \'e\', \'e\', \'e\'], [\'e\', \'e\', \'e\', \'e\', \'e\', \'e\', \'e\'], [\'e\', \'e\', \'e\', \'e\', \'e\', \'e\', \'e\'], [\'x\', \'e\', \'e\', \'e\', \'e\', \'e\', \'e\']]'


def test_envi_state_eq():
    state = EnviState()
    state_copy = state.copy()
    assert state == state_copy  

def test_envi_state_str():
    state = EnviState()
    assert str(state)
    
def test_envi_state_is_terminal_1():
    state = EnviState()
    assert not state.is_terminal() 
def test_envi_state_is_terminal_2():
    state = EnviState()
    state.cols = '6666666'
    assert state.is_terminal()
    
def test_envi_state_increase_col():
    state = EnviState()
    state.increase_col(0)
    assert state.cols.decode("ASCII") == '1000000'

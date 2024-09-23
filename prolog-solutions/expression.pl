:- load_library('alice.tuprolog.lib.DCGLibrary').

lookup(K, [(K, V) | _], V).
lookup(K, [_ | T], V) :- lookup(K, T, V).

variable(Name, variable(Name)).
const(Value, const(Value)).

operation(op_add, A, B, R) :- R is A + B.
operation(op_subtract, A, B, R) :- R is A - B.
operation(op_multiply, A, B, R) :- R is A * B.
operation(op_divide, _, 0, 0) :- !.
operation(op_divide, A, B, R) :- R is A / B.
operation(op_negate, A, R) :- R is -A.
operation(op_not, A, R) :- bool(A, B), R is 1 - B.
operation(op_and, A, B, R) :- bool(A, AB), bool(B, BB), R is AB /\ BB.
operation(op_or, A, B, R) :- bool(A, AB), bool(B, BB), R is AB \/ BB.
operation(op_xor, A, B, R) :- bool(A, AB), bool(B, BB), L is AB \/ BB, nand(A, B, P), R is L /\ P.
nand(A, B, R) :- operation(op_and, A, B, T), R is 1 - T.

bool(X, 1) :- X > 0.0.
bool(X, 0) :- X =< 0.0.

op_p(op_add) --> ['+'].
op_p(op_subtract) --> ['-'].
op_p(op_multiply) --> ['*'].
op_p(op_divide) --> ['/'].
op_p(op_negate) --> { atom_chars('negate', C) }, C.
op_p(op_not) --> ['!'].
op_p(op_and) --> { atom_chars('&&', C) }, C.
op_p(op_or) --> { atom_chars('||', C) }, C.
op_p(op_xor) --> { atom_chars('^^', C) }, C.

evaluate(const(Value), _, Value).
evaluate(variable(Name), Vars, R) :- atom_chars(Name, [N | _]), lookup(N, Vars, R).
evaluate(operation(Op, A, B), Vars, R) :-
    evaluate(A, Vars, AV),
    evaluate(B, Vars, BV),
    operation(Op, AV, BV, R).
evaluate(operation(Op, A), Vars, R) :-
    evaluate(A, Vars, AV),
    operation(Op, AV, R).

digits([]) --> [].
digits([H | T]) -->
  { member(H, ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '-'])},
  [H],
  digits(T).

nonvar(V, _) :- var(V).
nonvar(V, T) :- nonvar(V), call(T).

expr_p(variable(Name)) -->
  { nonvar(Name, atom_chars(Name, Chars)) },
  letters(Chars),
  { Chars = [_ | _], atom_chars(Name, Chars) }.

letters([]) --> [].
letters([H | T]) -->
  { member(H, [x, y, z, 'X', 'Y', 'Z'])},
  [H],
  letters(T).

expr_p(const(Value)) -->
  { nonvar(Value, number_chars(Value, Chars)) },
  digits(Chars),
  { Chars \= ['-'], Chars = [_ | _], number_chars(Value, Chars) }.
expr_p(operation(Op, A, B)) -->
    { get_whitespace(Op, WS) },
    ['('], expr_p(A), WS, op_p(Op), WS, expr_p(B), [')'].
expr_p(operation(Op, A)) -->
    { get_whitespace(Op, WS) },
    op_p(Op), WS, expr_p(A).

get_whitespace(Op, []) :- var(Op).
get_whitespace(Op, [' ']) :- \+ var(Op).

skip_ws([]) --> [].
skip_ws([' ' | T]) --> skip_ws(T).
skip_ws([H | T]) --> [H], skip_ws(T).

infix_str(E, A) :- ground(E), phrase(expr_p(E), C), atom_chars(A, C).
infix_str(E, A) :-   atom(A), atom_chars(A, C), phrase(skip_ws(C), R), phrase(expr_p(E), R), !.

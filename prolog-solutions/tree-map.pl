max(X, Y, Y) :- Y >= X.
max(X, Y, X) :- X > Y.

get_left(node(_, _, L, _, _), L).
get_right(node(_, _, _, R, _), R).
get_height(node(_, _, _, _, H), H).
get_height(nil, 0).
get_diff(nil, 0).
get_diff(A, B, Diff) :- get_diff(node(_, _, A, B, _), Diff).
get_diff(node(_, _, L, R, _), Diff) :- get_height(L, LH), get_height(R, RH), Diff is LH - RH.

parent_height(A, B, H) :- max(A, B, H1), H is H1 + 1.
calculate_height(L, R, H) :- get_height(L, LH), get_height(R, RH), parent_height(LH, RH, H).
make_node(K, V, L, R, node(K, V, L, R, H)) :- calculate_height(L, R, H).

map_get(node(K, V, _, _, _), K, V).
map_get(node(K, _, L, _, _), Key, Value) :- Key < K, map_get(L, Key, Value).
map_get(node(K, _, _, R, _), Key, Value) :- K < Key, map_get(R, Key, Value).

map_build([], nil).
map_build([(K, V) | T], TreeMap) :- map_build(T, Map), map_put(Map, K, V, TreeMap).

map_put(M, K, V, R) :- put(M, K, V, R, default).
map_putIfAbsent(M, K, V, R) :- put(M, K, V, R, if_absent).

put(nil, Key, Value, node(Key, Value, nil, nil, 1), _).
put(node(Key, _, L, R, H), Key, Value, node(Key, Value, L, R, H), default).
put(node(Key, Value, L, R, H), Key, _, node(Key, Value, L, R, H), if_absent).
put(node(Key, Val, L, R, _), K, V, Res, Mode) :- K < Key,
    put(L, K, V, NewL, Mode),
    make_node(Key, Val, NewL, R, TmpRes),
    reBalance(TmpRes, Res).
put(node(Key, Val, L, R, _), K, V, Res, Mode) :- K > Key,
    put(R, K, V, NewR, Mode),
    make_node(Key, Val, L, NewR, TmpRes),
    reBalance(TmpRes, Res).

reBalance(node(K, V, node(LK, LV, LL, LR, LH), R, _),
          node(LK, LV, LL, node(K, V, LR, R, HA), H)) :-
    get_diff(node(_, _, _, _, LH), R, 2),
    (get_diff(LL, LR,  0); get_diff(LL, LR,  1)), !,
    calculate_height(R, LR, HA),
    calculate_height(node(_, _, _, _, HA), LL, H).
reBalance(node(K, V, L, node(RK, RV, RL, RR, RH), _),
          node(RK, RV, node(K, V, L, RL, HA), RR, H)) :-
    get_diff(L, node(_, _, _, _, RH), -2),
    (get_diff(RL, RR,  0); get_diff(RL, RR,  -1)), !,
    calculate_height(L, RL, HA),
    calculate_height(node(_, _, _, _, HA), RR, H).
reBalance(node(K, V, node(LK, LV, LL, node(LRK, LRV, LRL, LRR, LRH), LH), R, _),
          node(LRK, LRV, node(LK, LV, LL, LRL, HB), node(K, V, LRR, R, HA), H)) :-
    get_diff(node(_, _, _, _, LH), R, 2),
    get_diff(LL, node(_, _, _, _, LRH),  -1), !,
    calculate_height(R, LRR, HA),
    calculate_height(LL, LRL, HB),
    parent_height(HA, HB, H).
% :NOTE: node constructor
reBalance(node(K, V, L, node(RK, RV, node(RLK, RLV, RLL, RLR, RLH), RR, RH), _),
          node(RLK, RLV, node(K, V, L, RLL, HA), node(RK, RV, RLR, RR, HB), H)) :-
    get_diff(L, node(_, _, _, _, RH), -2),
    get_diff(node(_, _, _, _, RLH), RR,  1), !,
    calculate_height(L, RLL, HA),
    calculate_height(RLR, RR, HB),
    parent_height(HA, HB, H).
reBalance(Node, Node).

map_remove(node(K, V, L, R, H), Key, Res) :- Key < K,
    map_remove(L, Key, NewL),
    reBalance(node(K, V, NewL, R, H), Res), !.
map_remove(node(K, Val, L, R, H), Key, Res) :- Key > K,
    map_remove(R, Key, NewR),
    reBalance(node(K, Val, L, NewR, H), Res), !.
map_remove(node(Key, _, L, R, _), Key, Res) :-
    remove_min_node(R, NewR, node(K, V, _, _, _)),
    calculate_height(L, NewR, HA),
    reBalance(node(K, V, L, NewR, HA), Res), !.
map_remove(node(Key, _, L, nil, _), Key, L) :- !.
map_remove(R, _, R).

remove_min_node(Node, R, Node) :- get_left(Node, nil), get_right(Node, R).
remove_min_node(node(Key, V, L, R, H), Res, LeftNode) :-
    remove_min_node(L, NewL, LeftNode),
    reBalance(node(Key, V, NewL, R, H), Res).

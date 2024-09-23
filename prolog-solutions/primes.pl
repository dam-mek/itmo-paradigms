prime(N) :- \+ composite(N).

cycle(I, N, STEP) :- I =< N, assert(composite(I)), I1 is I + STEP, cycle(I1, N, STEP), !.
sieve(I, N) :- I2 is I * I, I2 > N, !.
sieve(I, N) :- I2 is I * I, prime(I), cycle(I2, N, I), !.
sieve(I, N) :- I2 is I + 2, sieve(I2, N), !.

init(MAX_N) :- cycle(4, MAX_N, 2).
init(MAX_N) :- sieve(3, MAX_N).

next_divisor(N, A, A) :- 0 is mod(N, A), !.
next_divisor(N, A, R) :- A2 is A + 2, next_divisor(N, A2, R).

prime_divisors(1, []) :- !.
prime_divisors(N, [N]) :- prime(N), !.
prime_divisors(N, Divisors) :- \+ integer(N), !, product(N, Divisors).
prime_divisors(N, Divisors) :- 0 is mod(N, 2), !, divisors_of_N(N, 2, Divisors).
prime_divisors(N, Divisors) :- next_divisor(N, 3, F), divisors_of_N(N, F, Divisors), !.

product(N, [N]) :- prime(N).
product(N, [P1, P2 | T]) :- prime(P1), P1 =< P2, product(N1, [P2 | T]), N is N1 * P1, !.

divisors_of_N(1, _, []) :- !.
divisors_of_N(N, 2, [2 | T]) :- New is div(N, 2), 0 is mod(New, 2), !, divisors_of_N(New, 2, T).
divisors_of_N(N, 2, [2 | T]) :- New is div(N, 2), divisors_of_N(New, 3, T), !.
divisors_of_N(N, F, [P | T]) :- next_divisor(N, F, P), New is div(N, P), divisors_of_N(New, P, T).

expand([], []).
expand([N, N | Numbers], [(N, Pow) | Compact]) :- Pow > 1, Pow1 is Pow - 1, expand([N | Numbers], [(N, Pow1) | Compact]), !.
expand([N | Numbers], [(N, 1) | Compact]) :- expand(Numbers, Compact).

fold([], []).
fold([N, N | Numbers], [(N, Pow) | Compact]) :- fold([N | Numbers], [(N, Pow1) | Compact]), Pow is Pow1 + 1, !.
fold([N | Numbers], [(N, 1) | Compact]) :- fold(Numbers, Compact).

compact_prime_divisors(N, Compact) :- integer(N), !, prime_divisors(N, Divisors), fold(Divisors, Compact).
compact_prime_divisors(N, Compact) :- expand(Divisors, Compact), prime_divisors(N, Divisors), !.

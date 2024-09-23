'use strict';

const variable = v => (x, y, z) => v === 'x' ? x : v === 'y' ? y : v === 'z' ? z : null;
const cnst = c => () => c;

const tokenToFunc = {};
const funcArity = {};
const consts = {};

//:note: constructor is a bad name
function constructor(func, label) {
    const result = (...args) => {
        funcArity[label] = args.length;
        return (x, y, z) => func(...args.map(arg => arg(x, y, z)));
    }
    tokenToFunc[label] = result;
    return result;
}

function constructor_const(c, label) {
    consts[label] = c;
    return cnst(c);
}

const add =      constructor((a, b) => a + b, '+');
const subtract = constructor((a, b) => a - b, '-');
const multiply = constructor((a, b) => a * b, '*');
const divide =   constructor((a, b) => a / b, '/');
const negate =   constructor(a => -a, 'negate');
const floor =    constructor(a => Math.floor(a), '_');
const ceil =     constructor(a => Math.ceil(a), '^');
const madd =     constructor((a, b, c) => a * b + c, '*+');
const one =      constructor_const(1, 'one');
const two =      constructor_const(2, 'two');

const push = (stack, operation, ...args) => stack.push(operation(...args));

const variables = ['x', 'y', 'z'];

const updateStack = (stack, token) => {
    if (!isNaN(parseInt(token))) {
        push(stack, cnst, parseInt(token));
    } else if (token in consts) {
        push(stack, cnst, consts[token]);
    } else if (variables.includes(token)) {
        push(stack, variable, token);
    } else {
        const arity = funcArity[token];
        const args = stack.splice(-arity, arity);
        push(stack, tokenToFunc[token], ...args);
    }
};

const parse = expression => (x, y, z) => {
    const stack = [];
    expression.split(' ').filter(token => token.length).map(token => updateStack(stack, token));
    return stack[0](x, y, z);
};

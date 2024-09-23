"use strict";

const funcArity = {};

const tokenToFunc = {};

function AbstractOperation(...args) {
    this.args = args;
}

AbstractOperation.prototype.evaluate = function (x, y, z) {
    return this.func(...this.args.map(arg => arg.evaluate(x, y, z)));
}

AbstractOperation.prototype.toString = function () {
    return this.args.map(arg => arg.toString()).join(' ') + ' ' + this.sign;
}

AbstractOperation.prototype.prefix = function () {
    return '(' + this.sign + ' ' + this.args.map(arg => arg.prefix()).join(' ') + ')';
}

AbstractOperation.prototype.postfix = function () {
    return '(' + this.args.map(arg => arg.postfix()).join(' ') + ' ' + this.sign + ')';
}

function constructor_func(func, sign, diff_func) {
    function Element(...args) {
        AbstractOperation.call(this, ...args);
    }
    Element.prototype = Object.create(AbstractOperation.prototype);
    Element.prototype.constructor = Element;
    Element.prototype.sign = sign;
    Element.prototype.func = func;
    Element.prototype.diff = diff_func;
    tokenToFunc[sign] = Element;
    if (!(sign in funcArity)) {
        funcArity[sign] = func.length > 0 ? func.length : Infinity;
    }
    return Element;
}

const Negate = constructor_func(
    a => -a,
    'negate',
    function(x) {return new Negate(this.args[0].diff(x))},
);

const Add = constructor_func(
    (a, b) => a + b,
    '+',
    function(x) {return new Add(this.args[0].diff(x), this.args[1].diff(x))},
);

const Subtract = constructor_func(
    (a, b) => a - b,
    '-',
    function(x) {return new Subtract(this.args[0].diff(x), this.args[1].diff(x))},
);

const Multiply = constructor_func(
    (a, b) => a * b,
    '*',
    function(x) {return new Add(new Multiply(this.args[0].diff(x), this.args[1]), new Multiply(this.args[0], this.args[1].diff(x)))},
);

const Divide = constructor_func(
    (a, b) => a / b,
    '/',
    function(x) {return new Divide(new Subtract(new Multiply(this.args[0].diff(x), this.args[1]),
            new Multiply(this.args[0], this.args[1].diff(x))),
        new Multiply(this.args[1], this.args[1]))},
);

const Exp = constructor_func(
    a => Math.exp(a),
    null,
    function(x) {return new Multiply(this, this.args[0].diff(x))},
);

function constructor_sumsqN(n) {
    let sign = 'sumsq' + n;
    funcArity[sign] = n;
    return constructor_func(
        (...args) => args.map(x => x*x).reduce((x, y) => x + y),
        sign,
        function(var_name) {
            return this.args.map(x => new Multiply(x, x).diff(var_name)).reduce((x, y) => new Add(x, y));
        },
    );
}

function constructor_distanceN(n) {
    let sign = 'distance' + n;
    funcArity[sign] = n;
    const inner = constructor_sumsqN(n);
    return constructor_func(
        (...args) => inner.prototype.func(...args)**0.5,
        sign,
        function(var_name) {
            return new Multiply(
                new inner(...this.args).diff(var_name),
                new Divide(HALF_CONST, this)
            )
        },
    );
}

const Sumsq2 = constructor_sumsqN(2);
const Sumsq3 = constructor_sumsqN(3);
const Sumsq4 = constructor_sumsqN(4);
const Sumsq5 = constructor_sumsqN(5);

const Distance2 = constructor_distanceN(2);
const Distance3 = constructor_distanceN(3);
const Distance4 = constructor_distanceN(4);
const Distance5 = constructor_distanceN(5);

const Sumexp = constructor_func(
    (...args) => args.map(x => Math.exp(x)).reduce((x, y) => x + y, 0),
    'sumexp',
    function(var_name) {
        return this.args.map(x => new Exp(x).diff(var_name)).reduce((x, y) => new Add(x, y))
    },
);

const LSE = constructor_func(
    (...args) => Math.log(Sumexp.prototype.func(...args)),
    'lse',
    function(var_name) {
        return new Divide(
            new Sumexp(...this.args).diff(var_name),
            new Sumexp(...this.args)
        )
    },
);

function Const(cnst) {
    this.cnst = cnst;
    this.evaluate = function() { return this.cnst; };
    this.toString = function () { return cnst.toString(); };
    this.diff = function () { return ZERO_CONST; };
    this.prefix = function () { return this.toString(); };
    this.postfix = function () { return this.toString(); };
}

const ZERO_CONST = new Const(0);
const HALF_CONST = new Const(1/2);
const ONE_CONST = new Const(1);

function Variable(v) {
    this.v = v;
    this.evaluate = function(x, y, z) { return this.v === 'x' ? x : this.v === 'y' ? y : this.v === 'z' ? z : null; };
    this.toString = function () { return v.toString(); };
    this.diff = function (x) { return this.v === x ? ONE_CONST : ZERO_CONST; };
    this.prefix = function () { return this.toString(); }
    this.postfix = function () { return this.toString(); }
}

const variables = ['x', 'y', 'z'];

const push = (stack, operation, ...args) => stack.push(new operation(...args));

const updateStack = (stack, token) => {
    if (!isNaN(parseInt(token))) {
        push(stack, Const, parseInt(token));
    } else if (variables.includes(token)) {
        push(stack, Variable, token);
    } else {
        const arity = funcArity[token];
        const args = stack.splice(-arity, arity);
        push(stack, tokenToFunc[token], ...args);
    }
};

const parse = expression => {
    const stack = [];
    expression.split(' ').filter(token => token.length).map(token => updateStack(stack, token));
    return stack[0];
};


function ParseError(message) {
    this.message = message;
}
ParseError.prototype = Object.create(Error.prototype);
ParseError.prototype.name = 'ParseError';
ParseError.prototype.constructor = ParseError;
function constructor_error(name, message_format) {
    function Error(pos, ...args) {
        ParseError.call(this, message_format(...args) + ' at position ' + (pos + 1));
    }
    Error.prototype = Object.create(ParseError.prototype);
    Error.prototype.constructor = Error;
    Error.prototype.name = name;
    return Error;
}

const ExtraOperationError = constructor_error(
    'ExtraOperationError',
    (op1, op2) => 'Extra operation ' + op2 + ' when ' + op1 + ' exists'
)
const UnknownTokenError = constructor_error(
    'UnknownTokenError',
    token => 'Unknown token ' + token,
)
const UnexpectedTokenError = constructor_error(
    'UnexpectedTokenError',
    (expect, found) => 'Expected \'' + expect + '\', found \'' + found + '\''
)
const IllegalFunctionArityError = constructor_error(
    'IllegalFunctionArityError',
    (func, expect, found) => func + ' expects ' + expect + ' arguments but ' + found + (found > 1 ? ' were' : ' was') + ' found',
)
const ExtraSymbolsError = constructor_error(
    'ExtraSymbolsError',
    (count) => 'Extra ' + count + ' symbol' + (count > 1 ? 's' : '') + ' in expression',
)
const MissingBracketsError = constructor_error(
    'MissingBracketsError',
    (operation) => 'Missing brackets for operation ' + operation
)
const MissingOperandError = constructor_error(
    'MissingOperandError',
    (operation) => 'Not enough operands for \'' + operation + '\''
)
const NoOperationError = constructor_error(
    'NoOperationError',
    () => 'No operation in expression',
)
const ExtraArgumentError = constructor_error(
    'ExtraArgumentError',
    (operation) => 'Extra arguments after operation ' + operation + ' in expression',
)
const NoExpressionError = constructor_error(
    'NoExpressionError',
    () => 'Expression was not given',
)


const parseBrackets = (expression, start, method) => {
    let operation;
    const args = [];
    for (let i = start; i < expression.length; i++) {
        const token = expression[i].token;
        const pos = expression[i].pos;
        if (token === ')') {
            if (operation === undefined) {
                throw new NoOperationError(pos);
            }
            if (funcArity[operation] !== Infinity && args.length !== funcArity[operation]) {
                throw new IllegalFunctionArityError(pos, operation, funcArity[operation], args.length);
            }
            return {
                parsed: [new tokenToFunc[operation](...args)],
                start: i,
            };
        }
        if (token === '(') {
            const res = parseBrackets(expression, i + 1, method);
            args.push(res.parsed[0]);
            i = res.start;
            if (expression[i].token !== ')') {
                throw new UnexpectedTokenError(pos, ')', expression[i].token);
            }
        } else if (!isNaN(+token)) {
            if (method === 'post' && operation !== undefined) {
                throw new ExtraArgumentError(pos, operation);
            }
            args.push(new Const(+token));
        } else if (variables.includes(token)) {
            if (method === 'post' && operation !== undefined) {
                throw new ExtraArgumentError(pos, operation);
            }
            args.push(new Variable(token));
        } else if (token in tokenToFunc) {
            if (operation !== undefined) {
                throw new ExtraOperationError(pos, token, operation);
            }
            if (method === 'pre' && args.length !== 0) {
                throw new MissingOperandError(pos, token);
            }
            if (method === 'pre' && i === 0) {
                throw new MissingBracketsError(pos, token);
            }
            if (method === 'post' && funcArity[token] !== Infinity && args.length !== funcArity[token]) {
                throw new IllegalFunctionArityError(pos, token, funcArity[token], args.length);
            }
            operation = token;
        } else {
            throw new UnknownTokenError(pos, token);
        }
    }
    if (operation !== undefined) {
        if (args.length < funcArity[operation]) {
            throw new MissingOperandError(expression[expression.length - 1].pos, operation);
        } else {
            throw new MissingBracketsError(expression[expression.length - 1].pos, operation);
        }
    }
    return {
        parsed: args,
        start: expression.length,
    }
};

const parseExpression = (expression) => {
    let token = '';
    const parsedExpression = [];
    const addToken = function (index) {
        if (token.length !== 0) {
            parsedExpression.push({
                token: token,
                pos: index - token.length,
            });
            token = '';
        }
    }
    for (let i = 0; i < expression.length; i++) {
        let char = expression[i];
        if (char === ')' || char === '(' || char === ' ') {
            addToken(i);
            if (char !== ' ') {
                parsedExpression.push({
                    token: char,
                    pos: i,
                });
            }
        } else {
            token += char;
        }
    }
    addToken(expression.length);
    return parsedExpression;
}

const preParse = (expression, method) => {
    const parsedExpression = parseExpression(expression);
    if (parsedExpression.length === 0) {
        throw new NoExpressionError(-1);
    }
    const result = parseBrackets(parsedExpression, 0, method);
    if (result.parsed.length !== 1 || result.start !== parsedExpression.length) {
        if (result.parsed.length !== 1) {
            result.start = parsedExpression.length - 1;
        }
        const lastTokenPos = parsedExpression[result.start].pos;
        throw new ExtraSymbolsError(lastTokenPos, expression.length - lastTokenPos);
    }
    return result.parsed[0];
}

const parsePrefix = (expression) => {
    return preParse(expression, 'pre');
};

const parsePostfix = (expression) => {
    return preParse(expression, 'post');
};

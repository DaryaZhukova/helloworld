def expression = "1+2+3*4/2-6"
def operator = ["+","-","/","*"]
println expression

def operators = []
def myarr = []
def multiply = { x, y -> return x * y }
def division = { x, y -> return x / y }
def summing = { x, y -> return x + y }
def substract = {int x, int y -> return x - y }

def get_digit(expression, myarr) {
    myarr.add(expression.split('\\*|\\+|/|-')[0])
    return myarr
}

def get_operator(expression) {
    return expression.split(/\d/)[0]
}

def string_cut(expression, num) {
    expression = expression.substring(num)
    return expression
}

def weigth_off(my_operator) {
    def result
    switch (my_operator) {
        case ['+', '-']:
            result = 1
            break
        case ['*', '/']:
            result = 2
            break
    }
    return result
}

def calculation(arr, oper) {

    for ( i in 0..(arr.size()-1)) {
        if (arr[i] in oper) {
            switch (arr[i]) {
                case '*' :
                    res = arr[i -2].toInteger() * arr[i -1].toInteger()
                    break
                case '/' :
                    res = arr[i -2].toInteger() / arr[i -1].toInteger()
                    break
                case '+' :
                    res = arr[i -2].toInteger() + arr[i -1].toInteger()
                    break
                case '-' :
                    res = arr[i -2].toInteger() + arr[i -1].toInteger()
                    break
            }
            arr[i] = res
            2.times {
                arr.removeAt(i -1)
            }

        }
    }
    println res
    }


for (i in 1..110) {
    get_digit(expression, myarr)

    expression = string_cut(expression, myarr.last().length())

    if (expression.length() == 0) {
        myarr = myarr + operators.reverse()
        break
    }
    if (operators.size() == 0) {
        operators.add(get_operator(expression))
        expression = string_cut(expression, 1)
        continue
    }
    if (weigth_off(get_operator(expression)) > weigth_off(operators.last())) {
        operators.add(get_operator(expression))
        expression = string_cut(expression, 1)
    } else {
        myarr.add(operators.last())
        operators.removeLast()
        operators.add(get_operator(expression))
        expression = string_cut(expression, 1)
    }

}

println myarr
println operators

def q = substract(4, 5)

    calculation(myarr, operator)
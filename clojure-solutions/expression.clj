(def constant constantly)
(defn variable [var-name] #(get % (str var-name)))

(defn my-div
  ([a] (/ 1.0 a))
  ([a & args] (/ (double a) (apply * args))))
(defn sumexp-operation [& args] (apply + (map #(Math/exp %) args)))
(defn lse-operation [& args] (Math/log (apply sumexp-operation args)))

(defn do-operation [op & operations] (fn [map] (apply op (mapv #(% map) operations))))
(defn operation-constructor [op] (partial do-operation op))

(def subtract (operation-constructor -))
(def add (operation-constructor +))
(def divide (operation-constructor my-div))
(def multiply (operation-constructor *))
(def negate subtract)
(def sumexp (operation-constructor sumexp-operation))
(def lse (operation-constructor lse-operation))

(defn parsed-parser-constructor [const-func var-func ops]
    (fn [parsed]
        (cond
            (list? parsed) (apply (get ops (first parsed))
                                  (map (parsed-parser-constructor const-func var-func ops) (rest parsed)))
            (number? parsed) (const-func parsed)
            :else (var-func parsed))))


(defn parser-constructor [const-func var-func ops]
    (fn [expression]
        ((parsed-parser-constructor const-func var-func ops) (read-string expression))))

(def operations {
    '+ add
    '- subtract
    '* multiply
    '/ divide
    'negate negate
    'sumexp sumexp
    'lse lse
})

(def parseFunction (parser-constructor constant variable operations))

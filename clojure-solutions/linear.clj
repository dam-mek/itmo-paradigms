; / ----------- CONDITIONS ----------- /

(defn vector-of-number? [v] (and
                              (coll? v)
                              (every? number? v)
                              ))
(defn length-equals-to? [vector n] (= (count vector) n))
(defn same-size? [vs] (let [n (count (first vs))] (every? #(length-equals-to? % n) vs)))
(defn vectors-of-number? [vs] (every? vector-of-number? vs))
(defn valid-vectors? [vs] (and
                            (coll? vs)
                            (vectors-of-number? vs)
                            (same-size? vs)
                            ))
(defn valid-matrices? [ms] (and
                             (coll? ms)
                             (every? valid-vectors? ms)
                             (same-size? ms)
                             ))

(defn clear [tensor] (if (number? tensor) 
                       0 
                       (mapv clear tensor)))


(defn valid-tensor? [tensor]
  (or (vector-of-number? tensor)
      (and (vector? tensor)
           (apply = (clear tensor))
           (mapv valid-tensor? tensor))))


; / ----------- CONSTRUCTORS ----------- /

(defn create-vector-func [operation]
  (fn [& args]
    {:pre [(valid-vectors? args)]
     :post [(valid-vectors? [(first args) %])]}
    (apply mapv operation args)))

(defn create-matrix-func [operation]
  (fn [& args]
    {:pre [(valid-matrices? args)]
     :post [(valid-matrices? [(first args) %])]}
    (apply mapv operation args)))

(defn innerOperation [operation & args] 
  (if (vector-of-number? args)
    (apply operation args)
    (apply mapv (partial innerOperation operation) args)))

(defn create-tensor-func [operation]
    (fn [& args] 
      {:pre [(valid-tensor? (vec args))]
       :post [(valid-tensor? [%])]}
        (apply (partial innerOperation operation) args)))

; / ----------- VECTORS ----------- /

(def v+ (create-vector-func +))
(def v- (create-vector-func -))
(def v* (create-vector-func *))
(def vd (create-vector-func /))

(defn v*s [v & s]
    {:pre [(vector-of-number? v)
           (or (nil? s)
               (vector-of-number? s))]
     :post [(valid-vectors? [% v])]}
   (let [S (reduce * s)] (mapv #(* % S) v)))

(defn scalar [& vs]
  {:pre [(valid-vectors? vs)]
   :post [(number? %)]}
  (apply + (apply v* vs)))

(defn vect [& vs]
  {:pre [(coll? vs)
         (vectors-of-number? vs)
         (every? #(length-equals-to? % 3) vs)]
   :post [(vector-of-number? %)
          (length-equals-to? % 3)]}
  (reduce (fn [[a0 a1 a2] [b0 b1 b2]] [(- (* a1 b2) (* a2 b1))
                                       (- (* a2 b0) (* a0 b2))
                                       (- (* a0 b1) (* a1 b0))]) vs))

; / ----------- MATRICES ----------- /

(def m+ (create-matrix-func v+))
(def m- (create-matrix-func v-))
(def m* (create-matrix-func v*))
(def md (create-matrix-func vd))

(defn m*s [m & s]
  {:pre [(valid-matrices? [m]) (every? number? s)]
   :post [(valid-matrices? [m %])]}
  (let [S (reduce * s)] (mapv (fn [v] (mapv #(* % S) v)) m)))

(defn m*v [m v]
  {:pre [(valid-vectors? m)
         (vector-of-number? v)
         (same-size? [v (first m)])]
   :post [(vector-of-number? %)]}
  (mapv #(scalar v %) m))

(defn transpose [m]
  {:pre [(coll? m)
         (valid-vectors? m)]
   :post [(valid-vectors? %)
          (length-equals-to? % (count (first m)))
          (or (empty? %)
              (length-equals-to? (first %) (count m)))]}
  (apply mapv vector m))

(defn m*m [& ms]
  {:pre [(every? coll? ms)
         (every? valid-vectors? ms)]
   :post [(valid-vectors? %)]}
   (reduce
     (fn [x y]
       (let [yT (transpose y)]
         (mapv
           (fn [v]
             (m*v yT v))
           x)))
     ms))


; / ----------- TENSORS ----------- /

(def t+ (create-tensor-func +))
(def t- (create-tensor-func -))
(def t* (create-tensor-func *))
(def td (create-tensor-func /))

(ns tp2.core
    (:gen-class))

(defn move-pc [state]
      (let [{:keys [pc direction toroid]} state
            [x y] pc
            [direction-x direction-y] direction
            new-x (mod (+ x direction-x) 80)
            new-y (mod (+ y direction-y) 25)]
           (assoc state :pc [new-x new-y])))

; revisar esto que parece 0 respetar principios de programación funcional
(defn skip-next-cell [state]
      (move-pc state))

(defn greater [a b] (if (< a b) 1 0))

(def function-mapping
  { \+ +
   \- -
   \* *
   \/ /
   \% mod })

(def directions
  { \_ [[1 0] [-1 0]]
   \| [[0 1] [0 -1]] })

(defn read-program [file-path]
      (let [lines (clojure.string/split-lines (slurp file-path))]
           (vec (map #(vec (concat % (repeat (- 80 (count %)) \space))) lines))))

(defn init-toroid [program]
      (vec (concat program (repeat (- 25 (count program)) (vec (repeat 80 \space))))))

(defn print-toroid [toroid]
      (doseq [row toroid]
             (println (apply str row))))


(defn init-state [toroid]
      {:toroid toroid
       :pc [0 0]
       :direction [1 0]
       :stack '()
       :string-mode false})

(defn read-integer []
      (Integer/parseInt (read-line)))

(defn read-character []
      (int (first (read-line))))

(defn operation [stack operator]
      (let [a (first stack)
            b (second stack)
            rest (drop 2 stack)
            a-num (Integer/parseInt (str a))
            b-num (Integer/parseInt (str b))
            result (cond
                     (= operator /) (if (zero? a-num) (throw (Exception. "Divisor cannot be zero")) (quot b-num a-num))
                     (= operator mod) (mod b-num a-num)
                     :else (operator b-num a-num))
            new-stack (cons result rest)]
           new-stack))

(defn update-direction [state possible-direction]
      (let [stack (:stack state)
            first-element (Integer/parseInt (str (first stack)))
            new-direction (if (zero? first-element) (first possible-direction) (second possible-direction))]
           (assoc state :direction new-direction :stack (rest stack))))

(defn position-conversor [a scale]
      (if (< a scale) a (position-conversor (- a scale) scale)))

(defn execute-command [state]
      (let [{:keys [toroid pc stack string-mode]} state
            [x y] pc
            command (get-in toroid [y x])]
           ;(println "Posicion fila: " y " columna: " x " comando: " command, " stack: " stack)
           ;(print-toroid toroid)

           (cond
             (nil? command) (throw (Exception. "Command is nil"))

             string-mode
             (if (= command \")
               (assoc state :string-mode false)
               (update state :stack conj (int command)))

             (= command \")
             (assoc state :string-mode true)

             (= command \!)
             (let [a (first stack)
                   rest (rest stack)
                   new-stack (cons (if (zero? a) 1 0) rest)]
                  (assoc state :stack new-stack))

             (= command \$)
             (let [new-stack (rest stack)]
                  (assoc state :stack new-stack))

             (= command \`)
             (let [a (Integer/parseInt (str (first stack)))
                   b (Integer/parseInt (str (second stack)))
                   rest (drop 2 stack)
                   new-stack (cons (greater a b) rest)]
                  (assoc state :stack new-stack))

             (= command \\)
             (cond
               (empty? stack)
               (let [new-stack '(0 0)]
                    (assoc state :stack new-stack))

               (= 1 (count stack))
               (let [a (first stack)
                     new-stack (cons 0 stack)]
                    (assoc state :stack new-stack))

               :else
               (let [a (first stack)
                     b (second stack)
                     rest (drop 2 stack)
                     new-stack (cons b (cons a rest))]
                    (assoc state :stack new-stack)))

             (= command \&)
             (let [input (read-integer)
                   new-stack (cons input stack)]
                  (assoc state :stack new-stack))

             (= command \~)
             (let [input (read-character)
                   new-stack (cons input stack)]
                  (assoc state :stack new-stack))

             (or (= command \_) (= command \|))
             (let [new-direction (get-in directions [command])]
                  (update-direction state new-direction))

             (= command \:)
             (let [a (first stack)
                   new-stack (conj stack a)]
                  (assoc state :stack new-stack))

             (= command \#)
             (skip-next-cell state)

             (= command \g)
             (if (or (> (Integer/parseInt (str (first stack))) 24) (> (Integer/parseInt (str (second stack))) 79))
               (let [rest (drop 2 stack)
                     new-stack (cons 0 rest)]
                    (assoc state :stack new-stack))

               (let [y (Integer/parseInt (str (first stack)))
                     x (Integer/parseInt (str (second stack)))
                     rest (drop 2 stack)
                     value (get-in toroid [y x])
                     new-stack (cons (int value) rest)]
                    (assoc state :stack new-stack)))

             (= command \p)
             (let [y (position-conversor (Integer/parseInt (str (first stack))) 25)
                   x (position-conversor (Integer/parseInt (str (second stack))) 80)
                   value (Integer/parseInt (str (nth stack 2)))
                   new-stack (drop 3 stack)
                   new-toroid (assoc-in toroid [y x] (char value))]
                  (assoc state :stack new-stack :toroid new-toroid))

             (= command \,)
             (let [char-to-print (if (empty? stack) (char 0) (char (first stack)))
                   new-stack (if (empty? stack) stack (rest stack))]
                  (print char-to-print)
                  (assoc state :stack new-stack))

             (= command \.)
             (do
               (print (Integer/parseInt (str (first stack))))
               (let [new-stack (rest stack)]
                    (assoc state :stack new-stack)))

             :else
             (if-let [operator (function-mapping command)]
                     (let [new-stack (operation stack operator)]
                          (assoc state :stack new-stack))
                     (case command
                           \> (assoc state :direction [1 0])
                           \< (assoc state :direction [-1 0])
                           \^ (assoc state :direction [0 -1])
                           \v (assoc state :direction [0 1])
                           \@ (assoc state :halt true)
                           \? (assoc state :direction (rand-nth [[1 0] [-1 0] [0 1] [0 -1]]))
                           (if (not= (int \space) (int command)) (assoc state :stack (conj stack command)) state))))))

(defn run-program [state]
      (loop [current-state state]
            (if (:halt current-state)
              current-state
              (let [new-state (-> current-state
                                  execute-command
                                  move-pc)]
                   (recur new-state)))))

(defn -main [& args]
      (let [file-path (first args)
            program (read-program file-path)
            toroid (init-toroid program)
            initial-state (init-state toroid)]
           (run-program initial-state)))
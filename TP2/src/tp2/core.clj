(ns tp2.core
    (:gen-class))

(defn move-pc [state]
      (let [{:keys [pc direction toroid]} state
            [x y] pc
            [direction-x direction-y] direction
            new-x (mod (+ x direction-x) 80)
            new-y (mod (+ y direction-y) 25)]
           (assoc state :pc [new-x new-y])))


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

(defn init-toroid [program] (vec (concat program (repeat (- 25 (count program)) (vec (repeat 80 \space))))))

(defn update-direction [state possible-direction]
      (let [stack (:stack state)
            first-element (Integer/parseInt (str (first stack)))
            new-direction (if (zero? first-element) (first possible-direction) (second possible-direction))]
           (assoc state :direction new-direction :stack (rest stack))))

(defn init-state [toroid]
      {:toroid toroid
       :pc [0 0]
       :direction [1 0]
       :stack '()
       :string-mode false})

(defn position-conversor [a scale] (if (< a scale) a (position-conversor (- a scale) scale)))


(defn read-integer [] (Integer/parseInt (read-line)))

(defn read-character [] (int (first (read-line))))

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

(defn handle-string-mode [state command] (if (= command \") (assoc state :string-mode false) (update state :stack conj (int command))))

(defn handle-exclamation [state]
      (let [a (first (:stack state))
            rest (rest (:stack state))
            new-stack (cons (if (zero? a) 1 0) rest)]
           (assoc state :stack new-stack)))

(defn handle-pesito [state] (assoc state :stack (rest (:stack state))))

(defn handle-tick [state]
      (let [a (Integer/parseInt (str (first (:stack state))))
            b (Integer/parseInt (str (second (:stack state))))
            rest (drop 2 (:stack state))
            new-stack (cons (greater a b) rest)]
           (assoc state :stack new-stack)))

(defn handle-backslash [state]
      (let [stack (:stack state)]
           (cond
             (empty? stack)
             (assoc state :stack '(0 0))

             (= 1 (count stack))
             (assoc state :stack (cons 0 stack))

             :else
             (let [a (first stack)
                   b (second stack)
                   rest (drop 2 stack)
                   new-stack (cons b (cons a rest))]
                  (assoc state :stack new-stack)))))

(defn handle-ampersand [state]
      (let [input (read-integer)
            new-stack (cons input (:stack state))]
           (assoc state :stack new-stack)))

(defn handle-tilde [state]
      (let [input (read-character)
            new-stack (cons input (:stack state))]
           (assoc state :stack new-stack)))

(defn handle-guion-bajo-or-pipe [state command] (let [new-direction (get-in directions [command])] (update-direction state new-direction)))

(defn handle-2-puntos [state] (let [a (first (:stack state)) new-stack (conj (:stack state) a)] (assoc state :stack new-stack)))

(defn handle-g [state]
      (let [stack (:stack state)
            toroid (:toroid state)
            y (Integer/parseInt (str (first stack)))
            x (Integer/parseInt (str (second stack)))
            rest (drop 2 stack)]
           (if (or (> y 24) (> x 79))
             (assoc state :stack (cons 0 rest))
             (let [value (get-in toroid [y x])
                   new-stack (cons (int value) rest)]
                  (assoc state :stack new-stack)))))

(defn handle-p [state]
      (let [y (position-conversor (Integer/parseInt (str (first (:stack state)))) 25)
            x (position-conversor (Integer/parseInt (str (second (:stack state)))) 80)
            value (Integer/parseInt (str (nth (:stack state) 2)))
            new-stack (drop 3 (:stack state))
            new-toroid (assoc-in (:toroid state) [y x] (char value))]
           (assoc state :stack new-stack :toroid new-toroid)))

(defn handle-coma [state]
      (let [char-to-print (if (empty? (:stack state)) (char 0) (char (first (:stack state))))
            new-stack (if (empty? (:stack state)) (:stack state) (rest (:stack state)))]
           (print char-to-print)
           (assoc state :stack new-stack)))

(defn handle-puntazo [state] (print (Integer/parseInt (str (first (:stack state))))) (assoc state :stack (rest (:stack state))))

(defn handle-direction [state direction] (assoc state :direction direction))

(defn handle-halt [state](assoc state :halt true))

(defn handle-random-direction [state] (assoc state :direction (rand-nth [[1 0] [-1 0] [0 1] [0 -1]])))

(defn handle-default [state command] (if (not= (int \space) (int command)) (assoc state :stack (conj (:stack state) command)) state))

(defn execute-command [state]
      (let [{:keys [toroid pc stack string-mode]} state
            [x y] pc
            command (get-in toroid [y x])]

           (cond
             (nil? command) (throw (Exception. "Command is nil"))

             string-mode (handle-string-mode state command)

             (= command \") (assoc state :string-mode true)

             (= command \!) (handle-exclamation state)

             (= command \$) (handle-pesito state)

             (= command \`) (handle-tick state)

             (= command \\) (handle-backslash state)

             (= command \&) (handle-ampersand state)

             (= command \~) (handle-tilde state)

             (or (= command \_) (= command \|)) (handle-guion-bajo-or-pipe state command)

             (= command \:) (handle-2-puntos state)

             (= command \#) (move-pc state)

             (= command \g) (handle-g state)

             (= command \p) (handle-p state)

             (= command \,) (handle-coma state)

             (= command \.) (handle-puntazo state)

             (= command \>) (handle-direction state [1 0])

             (= command \<) (handle-direction state [-1 0])

             (= command \^) (handle-direction state [0 -1])

             (= command \v) (handle-direction state [0 1])

             (= command \@) (handle-halt state)

             (= command \?) (handle-random-direction state)

             (= command \>) (handle-direction state [1 0])

             (= command \<) (handle-direction state [-1 0])

             (= command \^) (handle-direction state [0 -1])

             (= command \v) (handle-direction state [0 1])

             (= command \@) (handle-halt state)

             (= command \?) (handle-random-direction state)

             :else (if-let [operator (function-mapping command)]
                           (let [new-stack (operation stack operator)]
                                (assoc state :stack new-stack))
                           (handle-default state command)))))

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
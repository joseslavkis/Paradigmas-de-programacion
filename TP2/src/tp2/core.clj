(ns tp2.core
    (:gen-class))

(defn move-pc [state]
      (let [{:keys [pc direction toroid]} state
            [x y] pc
            [direction-x direction-y] direction
            new-x (mod (+ x direction-x) 80)
            new-y (mod (+ y direction-y) 25)]
           (assoc state :pc [new-x new-y])))

;revisar esto que parece 0 respetar principios de programación funcional
(defn skip-next-cell [state]
      (move-pc state))

(defn greater [a b] (if (< a b) 1 0))

(def function-mapping
  { \+ +
   \- -
   \* *
   \/ /
   \% mod
   \` greater })

(def directions
  { \_ [[1 0] [-1 0]]
   \| [[0 1] [0 -1]] })

(defn read-program [file-path]
      (let [lines (clojure.string/split-lines (slurp file-path))]
           (vec (map #(vec (concat % (repeat (- 80 (count %)) \space))) lines))))

(defn init-toroid [program]
      (vec (concat program (repeat (- 25 (count program)) (vec (repeat 80 \space))))))

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
            b-num (Integer/parseInt (str b))]
           (let [result (operator b-num a-num)
                 new-stack (cons result rest)]
                new-stack)))

(defn update-direction [state possible-direction]
      (let [stack (:stack state)
            first-element (Character/digit (first stack) 10)
            new-direction (if (zero? first-element) (first possible-direction) (second possible-direction))]
           (assoc state :direction new-direction :stack (rest stack))))

(defn execute-command [state]
      (let [{:keys [toroid pc stack string-mode]} state
            [x y] pc
            command (get-in toroid [y x])]
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

             (= command \\)
             (let [a (first stack)
                   b (second stack)
                   rest (drop 2 stack)
                   new-stack (cons b (cons a rest))]
                  (assoc state :stack new-stack))

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

             (= command \.)
             (do
               (print (first stack))
               (let [new-stack (rest stack)]
                    (assoc state :stack new-stack)))

             (= command \#)
             (skip-next-cell state)

             (= command \,)
             (do
               (print (char (first stack)))
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
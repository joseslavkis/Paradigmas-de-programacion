(ns tp2.core
    (:gen-class))

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

(defn move-pc [state]
      (let [{:keys [pc direction toroid]} state
            [x y] pc
            [direction-x direction-y] direction
            new-x (mod (+ x direction-x) 80)
            new-y (mod (+ y direction-y) 25)]
           (assoc state :pc [new-x new-y])))

(defn operation [stack operator]
      (let [a (first stack)
            b (second stack)
            rest (drop 2 stack)]
           (cons (operator b a) rest)))

(defn update-direction [stack directions]
      (if (= (count stack) 0) (first directions) (second directions)))

(defn execute-command [state]
      (let [{:keys [toroid pc stack string-mode]} state
            [x y] pc
            command (get-in toroid [y x])]
           (cond
             string-mode
             (if (= command \")
               (assoc state :string-mode false)

               (update state :stack (conj (int command))))

             (= command \")
             (assoc state :string-mode true)

             (= command \!)
             (let [a (first stack)
                   rest (rest stack)]
                  (assoc state :stack (cons (if (zero? a) 1 0) rest)))

             (= command \\)
             (let [a (first stack)
                   b (second stack)
                   rest (drop 2 stack)]
                  (assoc state :stack (cons b (cons a rest))))

             (or (= command \_) (= command \|))
             (update-direction (state :stack) (get-in directions [command]))

             (= command \:)
             (let [a (first stack)]
                  (assoc state :stack (conj stack a)))

             (= command \,)
             (do
               (print (char (first stack)))
               (assoc state :stack (rest stack)))

             :else
             (if-let [operator (function-mapping command)]
                     (assoc state :stack (operation stack operator))
                     (case command
                           \> (assoc state :direction [1 0])
                           \< (assoc state :direction [-1 0])
                           \^ (assoc state :direction [0 -1])
                           \v (assoc state :direction [0 1])
                           \@ (assoc state :halt true)
                           \? (assoc state :direction (rand-nth [[1 0] [-1 0] [0 1] [0 -1]]))
                           state)))))

(defn run-program [state]
      (loop [current-state state]
            (if (:halt current-state)
              current-state
              (do
                (let [new-state (-> current-state
                                    execute-command
                                    move-pc)]
                     (recur new-state))))))

(defn -main [& args]
      (let [file-path (first args)
            program (read-program file-path)
            toroid (init-toroid program)
            initial-state (init-state toroid)]
           (run-program initial-state)))
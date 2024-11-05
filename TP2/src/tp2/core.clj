(ns tp2.core
    (:gen-class))


;Aquí se le pasa a slurp un path y luego split-lines se guarda todas las lineas
;
(defn read-program [file-path]
      (let [lines (clojure.string/split-lines (slurp file-path))]
           (vec (map #(vec (concat % (repeat (- 80 (count %)) \space))) lines)))) ;la cantidad de espacios es 80 - count de elementos
;inicializo el toroide, program sería lo que devuelve read-program
(defn init-toroid [program]
      (vec (concat program (repeat (- 25 (count program)) (vec (repeat 80 \space))))))

(defn init-state [toroid]
      {:toroid toroid
       :pc [0 0]
       :direction [1 0]
       :stack []})

(defn move-pc [state]
      (let [{:keys [pc direction toroid]} state             ;extraigo las claves de state
            [x y] pc                                        ;extraigo las coordenadas de pc
            [dx dy] direction                               ;extraigo las coordenadas de direction
            new-x (mod (+ x dx) 80)                         ;calculo las nuevas coordenadas
            new-y (mod (+ y dy) 25)]                        ;calculo las nuevas coordenadas
           (assoc state :pc [new-x new-y])))                ;devuelvo el estado con las nuevas coordenadas(es medio mágica assoc)

(defn execute-command [state]
      (let [{:keys [toroid pc stack]} state                 ;extraigo las claves de state
            [x y] pc                                        ;extraigo las coordenadas de pc
            command (get-in toroid [y x])]                  ;primer fila despues col
           (case command
                 \> (assoc state :direction [1 0])
                 \< (assoc state :direction [-1 0])
                 \^ (assoc state :direction [0 -1])
                 \v (assoc state :direction [0 1])
                 \@ (assoc state :halt true)                ;si es @ termina
                 state)))

(defn run-program [state]
      (loop [state state]
            (if (:halt state)
              state
              (recur (-> state
                         execute-command
                         move-pc)))))
;programita main que lee el archivo, inicializa el toroide, el estado y corre el programa
(defn -main [& args]
      (let [file-path (first args)
            program (read-program file-path)
            toroid (init-toroid program)
            initial-state (init-state toroid)]
           (run-program initial-state)))



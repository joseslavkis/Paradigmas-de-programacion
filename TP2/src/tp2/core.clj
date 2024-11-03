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





(defproject tp2 "0.1.0-SNAPSHOT"
  :description "Befunge-93 Interpreter"
  :dependencies [[org.clojure/clojure "1.10.3"]]
  :main ^:skip-aot tp2.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
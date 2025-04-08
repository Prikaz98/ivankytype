(defproject ivankytype "0.1.0-SNAPSHOT"
  :description "My recreation of monkeytype"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring-core "1.14.1"]
                 [ring/ring-jetty-adapter "1.14.1"]
                 [org.clojure/clojurescript "1.11.132"]
                 [compojure/compojure "1.7.1"]
                 [hiccup "2.0.0-RC1"]]
  :repl-options {:init-ns ivankytype.core}

  :plugins [[lein-cljsbuild "1.1.8"]]

  :clean-targets ^{:protect false} [:target-path :compile-path "resources/public/js" "dev-target"]

  :uberjar-name "ivankytype.jar"

  :main ivankytype.core

  :cljsbuild {:builds
              [
;;               {:id "app"
;;                :source-paths ["src/cljs"]
;;                :compiler {:output-to "dev-target/public/js/compiled/ivankytype.js"
;;                           :output-dir "dev-target/public/js/compiled/out"
;;                           ;;:source-map-timestamp true
;;                           :optimizations :whitespace
;;                           :pretty-print true
;;                           }}
               {:id "min"
                :source-paths ["src/cljs" "src/cljc"]
                :jar true
                :compiler {:main ivankytype-ui.core
                           :output-to "resources/public/js/compiled/ivankytype.js"
                           :output-dir "target"
                           :source-map-timestamp true
                           :optimizations :advanced
                           :closure-defines {goog.DEBUG false}
                           :pretty-print false}}
               ]}
  )

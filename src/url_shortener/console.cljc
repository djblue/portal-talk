(ns url-shortener.console
  #?(:cljs (:require-macros portal.console)))

(defonce enabled (atom false))

(defn toggle [] (swap! enabled not))

(defn now []
  #?(:clj (java.util.Date.) :cljs (js/Date.)))

(defn run [f]
  (try
    [nil (f)]
    (catch #?(:clj Exception :cljs :default) ex#
      [:throw ex#])))

(defn runtime []
  #?(:bb :bb :clj :clj :cljs :cljs))

(defn capture [level form expr]
  (let [{:keys [line column]} (meta form)]
    `(if-not @enabled
       ~expr
       (let [[flow# result#] (run (fn [] ~expr))]
         (tap>
          {:form     (quote ~expr)
           :level    (if (= flow# :throw) :fatal ~level)
           :result   result#
           :ns       (quote ~(symbol (str *ns*)))
           :file     ~#?(:clj *file* :cljs nil)
           :line     ~line
           :column   ~column
           :time     (now)
           :runtime  (runtime)})
         (if (= flow# :throw) (throw result#) result#)))))

(defmacro log [expr] (capture :info &form expr))

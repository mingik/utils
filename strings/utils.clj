(ns strutils)

(defn blank? [str]
  (every? #(Character/isWhitespace %) str))


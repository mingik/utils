(ns utils.log)

;; Recieves writer and writes to it
(defn logger-core
  [writer]
  #(binding [*out* writer]
     (println %)))

;; Recieves filename and writes to the file with that filename
(require 'clojure.java.io)
(defn file-logger-core
  [filename]
  #(with-open [f (clojure.java.io/writer filename :append true)]
     ((logger-core f) %)))

;; Recieves many logger-cores and writes to them
(defn multi-logger
  [& logger-fns]
  #(doseq [f logger-fns]
     (f %)))

;; Tiemstamp wrapper
(defn timestamped-logger
  [logger-core]
  #(logger-core (format "[%1$tY-%1$tm-%1$te %1$tH:%1$tM:%1$tS] %2$s" (java.util.Date.) %)))

;; Usage 1: Console logger
(def *out*-logger (logger-core *out*))
(*out*-logger "hello goes to console")

;; Usage 2: Memory buffer logger
(def retained-logger (print-logger (java.io.StringWriter.)))
(retained-logger "hello goes to buffer")
(str writer)

;; Usage 3: File buffer logger
(def file-logger (file-logger-core "messages.log"))
(log->file "hello goes to file")

;; Usage 4: Multi logger
(def log (multi-logger
          (print-logger *out*)
          (file-logger "messages.log")))
(log "hello again")

;; Usage 5: Timestamped Multi logger
(def log-timestamped (timestamped-logger log))

(ns rubicks
  (:use clojure.test)
  )

(def innhold
[[1 2 3]
[4 5 6]
[7 8 9]]
)

(defn start-side [c]
[[c c c]
[c c c]
[c c c]]
)
                 

(def solved
  {
   :w (start-side :w)
   :y (start-side :y)
   :o (start-side :o)
   :r (start-side :r)
   :g (start-side :g)
   :b (start-side :b)

}
)


(with-test
  (defn rotate [side]
    [[ (first (last side))  (first (second side)) (first (first side)) ]
     [ (second (last side)) (second (second side)) (second (first side))]
     [ (last (last side))     (last (second side))   (last (first side))] 
    ]
  )
  
  (is (= [[7 4 1]
          [8 5 2]
          [9 6 3]]          
         
       (rotate [[1 2 3]
                  [4 5 6]
                  [7 8 9]]
         )
      
      )
     )
  )
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
    "Rotates a side clockwise"
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

(with-test 
  (defn turn-right-vertical [from-side into-side]
    [[(first (first into-side)) (second (first into-side)) (last (first from-side))]
     [(first (second into-side)) (second (second into-side)) (last (second from-side))]
     [(first (last into-side)) (second (last into-side)) (last (last from-side))]
     ]
    )
  (is (= [[:g :g :w] [:g :g :w] [:g :g :w]] (turn-right-vertical (solved :w) (solved :g))))
  )
     
  

(run-tests)
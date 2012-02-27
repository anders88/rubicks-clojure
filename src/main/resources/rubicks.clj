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
    "Turns one right vertical into another"
    [[(first (first into-side)) (second (first into-side)) (last (first from-side))]
     [(first (second into-side)) (second (second into-side)) (last (second from-side))]
     [(first (last into-side)) (second (last into-side)) (last (last from-side))]
     ]
    )
  (is (= [[:g :g :w] [:g :g :w] [:g :g :w]] (turn-right-vertical (solved :w) (solved :g))))
  )
     
(with-test 
  (defn turn-left-vertical [from-side into-side]
    "Turns one left vertical into another"
    [[(first (first from-side)) (second (first into-side)) (last (first into-side))]
     [(first (second from-side)) (second (second into-side)) (last (second into-side))]
     [(first (last from-side)) (second (last into-side)) (last (last into-side))]
     ]
    )
  (is (= [[:w :g :g] [:w :g :g] [:w :g :g]] (turn-left-vertical (solved :w) (solved :g))))
  )

(with-test
  (defn turn-top-horizontal [from-side into-side]
    (if (= :b (second (second into-side)))
      [(first into-side) (second into-side) (first from-side)]
      [(first from-side) (second into-side) (last into-side)]
    ))
 (is (= [[:r :r :r] [:g :g :g] [:g :g :g]] (turn-top-horizontal (solved :r) (solved :g))))
 (is (= [[:b :b :b] [:b :b :b] [:o :o :o]] (turn-top-horizontal (solved :o) (solved :b))))
)

(with-test
  (defn turn-bottom-horizontal [from-side into-side]
    (if (= :b (second (second into-side)))
      [(last from-side) (second into-side) (last into-side)]
      [(first into-side) (second into-side) (last from-side)]
    ))
 (is (= [[:g :g :g] [:g :g :g] [:r :r :r]] (turn-bottom-horizontal (solved :r) (solved :g))))
 (is (= [[:o :o :o] [:b :b :b] [:b :b :b]] (turn-bottom-horizontal (solved :o) (solved :b))))
)

(with-test
  (defn turn-r [cube]
    "Turns the right vertical. Offical notation R"
      {
       :w (turn-right-vertical (cube :g) (cube :w))
       :y (turn-right-vertical (cube :b) (cube :y))
       :o (cube :o)
       :r (rotate (cube :r))
       :g (turn-right-vertical (cube :y) (cube :g))
       :b (turn-right-vertical (cube :w) (cube :b))

       }

    )
  (is (= [[:b :b :w] [:b :b :w] [:b :b :w]] ((turn-r solved) :b)))  
  (is (= [[:y :y :b] [:y :y :b] [:y :y :b]] ((turn-r solved) :y)))
)

(with-test
  (defn turn-u[cube]
    "Turns the upper notation. Offical notation U"
      {
       :w (rotate (cube :w))
       :y (cube :y)
       :o (turn-top-horizontal (cube :g) (cube :o))
       :r (turn-top-horizontal (cube :b) (cube :r))
       :g (turn-top-horizontal (cube :r) (cube :g))
       :b (turn-top-horizontal (cube :o) (cube :b))

       }

    )
  (is (= [[:b :b :b] [:b :b :b] [:o :o :o]] ((turn-u solved) :b)))  
  (is (= [[:y :y :y] [:y :y :y] [:y :y :y]] ((turn-u solved) :y)))
  (is (= [[:r :r :r] [:g :g :g] [:g :g :g]] ((turn-u solved) :g)))
  
  (is (= [[:r :r :r] [:g :g :y] [:g :g :y]] ((turn-u (turn-r solved)) :g)))
  (is (= [[:w :w :w] [:w :w :w] [:g :g :g]] ((turn-u (turn-r solved)) :w)))
)




(run-tests)
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
  (defn turn-Right-vertical [from-side into-side]
    "Turns one right vertical into another"
    [[(first (first into-side)) (second (first into-side)) (last (first from-side))]
     [(first (second into-side)) (second (second into-side)) (last (second from-side))]
     [(first (last into-side)) (second (last into-side)) (last (last from-side))]
     ]
    )
  (is (= [[:g :g :w] [:g :g :w] [:g :g :w]] (turn-Right-vertical (solved :w) (solved :g))))
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
    (cond 
      (= :b (second (second into-side)))
      [(first into-side) (second into-side) (first from-side)]
      (= :b (second (second from-side)))
      [(last from-side) (second into-side) (last into-side)]
      :else
      [(first from-side) (second into-side) (last into-side)]
    ))
 (is (= [[:r :r :r] [:g :g :g] [:g :g :g]] (turn-top-horizontal (solved :r) (solved :g))))
 (is (= [[:b :b :b] [:b :b :b] [:o :o :o]] (turn-top-horizontal (solved :o) (solved :b))))
 (is (= [[:b :b :b] [:r :r :r] [:r :r :r]] (turn-top-horizontal (solved :b) (solved :r))))
)

(with-test
  (defn turn-bottom-horizontal [from-side into-side]
    (cond 
      (= :b (second (second into-side)))
      [(last from-side) (second into-side) (last into-side)]
      (= :b (second (second from-side)))
      [(first into-side) (second into-side) (first from-side)]
      :else
      [(first into-side) (second into-side) (last from-side)]
    ))
 (is (= [[:g :g :g] [:g :g :g] [:r :r :r]] (turn-bottom-horizontal (solved :r) (solved :g))))
 (is (= [[:o :o :o] [:b :b :b] [:b :b :b]] (turn-bottom-horizontal (solved :o) (solved :b))))
 (is (= [[:r :r :r] [:r :r :r] [:b :b :b]] (turn-bottom-horizontal (solved :b) (solved :r))))
)

(with-test
  (defn turn-R [cube]
    "Turns the right vertical up. Offical notation R"
      {
       :w (turn-Right-vertical (cube :g) (cube :w))
       :y (turn-Right-vertical (cube :b) (cube :y))
       :o (cube :o)
       :r (rotate (cube :r))
       :g (turn-Right-vertical (cube :y) (cube :g))
       :b (turn-Right-vertical (cube :w) (cube :b))

       }

    )
  (is (= [[:b :b :w] [:b :b :w] [:b :b :w]] ((turn-R solved) :b)))  
  (is (= [[:y :y :b] [:y :y :b] [:y :y :b]] ((turn-R solved) :y)))
)

(with-test
  (defn turn-r [cube]
    "Turns the right vertical down. Offical notation r"
    (-> cube turn-R turn-R turn-R)
    )
  (is (= (-> solved turn-R turn-R) (-> solved turn-r turn-r)))  
)




(with-test
  (defn turn-U[cube]
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
  (is (= [[:b :b :b] [:b :b :b] [:o :o :o]] ((turn-U solved) :b)))  
  (is (= [[:y :y :y] [:y :y :y] [:y :y :y]] ((turn-U solved) :y)))
  (is (= [[:r :r :r] [:g :g :g] [:g :g :g]] ((turn-U solved) :g)))
  
  (is (= [[:r :r :r] [:g :g :y] [:g :g :y]] ((turn-U (turn-R solved)) :g)))
  (is (= [[:w :w :w] [:w :w :w] [:g :g :g]] ((turn-U (turn-R solved)) :w)))
)

(with-test
  (defn turn-u [cube]
    "Turns the upper notation anti-clock. Offical notation U"
    (-> cube turn-U turn-U turn-U)
    )
  (is (= (-> solved turn-U turn-U) (-> solved turn-u turn-u)))  
)


(def max-turns 6)
(def possible-turns [turn-R turn-U])

(with-test
  (defn solve
    ([cube] (solve cube [] []))
    ([cube solution cube-paths]
    (let [find-solution (fn ! [cube solution moves cube-paths]
                          (if (empty? moves) []
                          (let [a-solution (solve ((first moves) cube) (conj solution (first moves)) (conj cube-paths cube))]
                            (if (empty? a-solution) (! cube solution (rest moves) cube-paths)
                              a-solution
                          ))))
                            
          ]
    (cond (= cube solved) solution                     
          (or (contains? (set cube-paths) cube) (= (count solution) max-turns)) []
          :else
          (find-solution cube solution possible-turns cube-paths)
    )        
    )))
  (is (= [] (solve solved)))
  (is (= [turn-R turn-R turn-R] (solve (turn-R solved))))
  (is (= [turn-R] (solve (-> solved turn-R turn-R turn-R))))
  (is (= [turn-U] (solve (-> solved turn-U turn-U turn-U))))
  (is (= [turn-U turn-U turn-U] (solve (turn-U solved))))
  (is (= [turn-R turn-R turn-R turn-U turn-U turn-U] (solve (turn-R (turn-U solved)))))
  )
  
(defn printable-solution [solution]
  (map (fn [step] (cond
                    (= step turn-U) "turn-U"
                    (= step turn-R) "turn-R"
                    :else "xxxx"))
       solution)
  ) 


(run-tests)
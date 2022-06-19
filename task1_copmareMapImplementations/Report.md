1. HashMap<>()
   - catch ConcurrentModificationException
   - 1000000 put operations took 36 - 4191 msec
   - 1000000 sum operations took 17 - 1670 msec
   
2. Collections.synchronizedMap()
   - catch ConcurrentModificationException
     - when we call map.values(), we should copy Collection to a new one and iterate throw it.
   - 1000000 put operations took 66 - 4078 msec
   - 1000000 sum operations took 48 - 4676 msec
   
3. ConcurrentHashMap<>()
   - no ConcurrentModificationException
   - 1000000 put operations took 47 - 4910 msec
   - 1000000 sum operations took 14 - 4712 msec

4. Custom map with synchronization
   - no ConcurrentModificationException
   - 1000000 put operations took 56 - 1532 msec
   - 1000000 sum operations took 4 - 726 msec

5. Custom map without synchronization
   - no ConcurrentModificationException
   - 1000000 put operations took too long
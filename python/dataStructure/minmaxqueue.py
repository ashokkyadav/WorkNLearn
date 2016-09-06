import sys
import math
class MinMaxQueue:
    # initializes a double-ended priority queue. data is optional
    def __init__(self, data = None):
        """ Creates a MinMaxQueue object"""
        self.min_max_q = [0]
        # if data is provided, insert all of it to min_max_q
        if data:
            for d in data:
                self.put(d)

    # check if the MinMaxQueue is empty
    def isEmpty(self):
        """ if the MinMaxQueue is empty. """
        return ((len(self.min_max_q) - 1) == 0)

    # returns the size of the MinMaxQueue
    def Size(self):
        """ Size of MinMaxQueue."""
        return len(self.min_max_q) - 1

    # builds a MinMaxQueue from the inputs from given file. File has first line as the
    # number of elements then all subsequent lines contain the elements
    def BuildMinMaxQueue(self, filename):
        """ builds a MinMaxQueue from the inputs from given file. """
        fh = open(filename)
        elems = int(fh.readline().rstrip())
        print elems, 'elements are going to be inserted in MinMaxQueue..'
        for line in fh:
            # print line
            self.put(line.rstrip())
        fh.close()

    def GetMin(self):
        """ returns the element with minimum priority."""
        sz = self.Size()
        # if heap contains at least one element
        if sz >= 1:
            return self.min_max_q[1]
        else:
            raise IndexError("Error!! No elements in the MinMaxQueue!!")

    def GetMax(self):
        """ Returns the element with maximum priority."""
        sz = self.Size()
        if sz >= 1:
            temp = [self.min_max_q[1]]
            if sz > 1:
                temp += [self.min_max_q[2]]
            if sz > 2:
                temp += [self.min_max_q[3]]
            return max(temp)
        else:
            raise IndexError("Error!! No elements in the MinMaxQueue!!")

    def put(self, element):
        """ inserts an element to the MinMaxQueue. """
        # append the entry
        self.min_max_q = self.min_max_q + [element]
        # Now Bubble-Up this to correct position
        self.swimUp(self.Size())

    def RemoveMin(self):
        """ Removes and returns the minimum element from min-max heap."""
        elem = self.GetMin()
        idx = 1
        self.deleteAtIndex(idx)
        return elem

    def RemoveMax(self):
        """ Removes and returns the maximum element from min-max heap."""
        elem = self.GetMax()
        # Get index of max element
        idx = self.min_max_q.index(elem)
        self.deleteAtIndex(idx)
        return elem


    def isContain(self, x):
        """ Returns TRUE if x is present in MinMaxHeap."""
        #print self.min_max_q
        return x in self.min_max_q

    def deleteAtIndex(self, idx):
        """ deletes an element from the given index."""
        # copy the last item to the given index
        self.min_max_q[idx] = self.min_max_q[-1]
        # delete last entry of list
        del(self.min_max_q[-1])
        # Now sinkDown this element to correct position
        self.sinkDown(idx)

    def sinkDown(self, idx):
        """ Sinks down an element from given index to its proper position."""
        # sink down appropriately according to min or max levels of current position
        if self.minLevel(idx):
            self.sinkDownMin(idx)
        else:
            self.sinkDownMax(idx)

    def sinkDownMin(self, idx):
        """ Sinks down an element which is on min level."""
        # if it has a child
        if self.hasChildren(idx):
            children = self.getChildren(idx)
            grandChildren = self.getGrandChildren(idx)
            ChildrenAndGrandChildren = children + grandChildren
            sorted_by_second = sorted(ChildrenAndGrandChildren, key=lambda tup: tup[1])
            idxOfMin, minOfChildrenAndGrandChildren = sorted_by_second[0]
            # print minOfChildrenAndGrandChildren, children, grandChildren
            # idxOfMin = self.min_max_q.index(minOfChildrenAndGrandChildren)
            # if minimum is children of current node.
            if (minOfChildrenAndGrandChildren in children):
                if minOfChildrenAndGrandChildren < self.min_max_q[idx]:
                    self.swapElements(idx, idxOfMin)
            else:
                # min is grandchild of current node.
                if minOfChildrenAndGrandChildren < self.min_max_q[idx]:
                    self.swapElements(idx, idxOfMin)
                    if (self.min_max_q[idxOfMin] > self.min_max_q[self.parentIndex(idxOfMin)]):
                        self.swapElements(idxOfMin, self.parentIndex(idxOfMin))
                    self.sinkDownMin(idxOfMin)

    def sinkDownMax(self, idx):
        """ Sinks down an element which is on min level."""
        # if it has a child
        if self.hasChildren(idx):
            children = self.getChildren(idx)
            grandChildren = self.getGrandChildren(idx)
            ChildrenAndGrandChildren = children + grandChildren
            sorted_by_second = sorted(ChildrenAndGrandChildren, key=lambda tup: tup[1])
            idxOfMax, maxOfChildrenAndGrandChildren = sorted_by_second[-1]
            # if maximum is children of current node.
            if (maxOfChildrenAndGrandChildren in children):
                if maxOfChildrenAndGrandChildren > self.min_max_q[idx]:
                    self.swapElements(idx, idxOfMax)
            else:
                # min is grandchild of current node.
                if maxOfChildrenAndGrandChildren > self.min_max_q[idx]:
                    self.swapElements(idx, idxOfMax)
                    if (self.min_max_q[idxOfMax] < self.min_max_q[self.parentIndex(idxOfMax)]):
                        self.swapElements(idxOfMax, self.parentIndex(idxOfMax))
                    self.sinkDownMax(idxOfMax)

    def hasChildren(self, idx):
        return idx * 2 <= self.Size()

    def getChildren(self, idx):
        # returns children of idx
        children = []
        children += [(idx * 2, self.min_max_q[idx * 2])]
        if (idx * 2) + 1 <= self.Size():
            children += [(idx * 2 + 1, self.min_max_q[idx * 2 + 1])]
        return children

    def getGrandChildren(self, idx):
        # returns grand children of idx if any
        gChildren = []
        if idx * 2 * 2 <= self.Size():
            gChildren += [(idx * 2 * 2, self.min_max_q[idx * 2 * 2])]
        if (idx * 2 * 2) + 1 <= self.Size():
            gChildren += [(idx * 2 * 2 + 1, self.min_max_q[(idx * 2 * 2) + 1])]
        if ((idx * 2 + 1) * 2) <= self.Size():
            gChildren += [((idx * 2 + 1) * 2, self.min_max_q[(idx * 2 + 1) * 2])]
        if ((idx * 2 + 1) * 2) + 1 <= self.Size():
            gChildren += [(((idx * 2 + 1) * 2) + 1, self.min_max_q[(idx * 2 + 1) * 2 + 1])]
        return gChildren

    def swimUp(self, l):
        """ Swims up an element from last position to correct one."""
        if self.parentIndex(l) != 0:
            if self.minLevel(l):
                # parentNode is max Node.
                if self.min_max_q[l] > self.min_max_q[self.parentIndex(l)]:
                    # max Heap violation. Correct it
                    self.swapElements(l, self.parentIndex(l))
                    self.swimUpMax(self.parentIndex(l))
                else:
                    self.swimUpMin(l)
            else:
                # parentNode is min Node
                if self.min_max_q[l] < self.min_max_q[self.parentIndex(l)]:
                    # min Heap violation. Correct it.
                    self.swapElements(l, self.parentIndex(l))
                    self.swimUpMin(self.parentIndex(l))
                else:
                    self.swimUpMax(l)

    def swimUpMax(self, idx):
        """Helper function: Swims up in the max heap levels."""
        # if idx has a grandparent
        if self.parentIndex(self.parentIndex(idx)) != 0:
            grandParentidx = self.parentIndex(self.parentIndex(idx))
            if self.min_max_q[idx] > self.min_max_q[grandParentidx]:
                # violates max Heap property! swap to correct it
                self.swapElements(idx, grandParentidx)
                # recursively do it.
                self.swimUpMax(grandParentidx)

    def swimUpMin(self, idx):
        """Helper function: Swims up in the min heap levels."""
        # if idx has grandparent
        if self.parentIndex(self.parentIndex(idx)) != 0:
            grandParentidx = self.parentIndex(self.parentIndex(idx))
            if self.min_max_q[idx] < self.min_max_q[grandParentidx]:
                # violates min heap property. Swap to fix it.
                self.swapElements(idx, grandParentidx)
                # fix it recursively
                self.swimUpMin(grandParentidx)

    def parentIndex(self, position):
        """ Returns index of child's parent which is at floor of half this index."""
        return position // 2

    def swapElements(self, i, j):
        """Helper Function to swap values at indices i and j."""
        a = self.min_max_q[i]
        self.min_max_q[i] = self.min_max_q[j]
        self.min_max_q[j] = a

    def level(self, position):
        """ returns the level of element in binary heap tree"""
        return int(math.floor(math.log(position, 2))) + 1

    def minLevel(self, position):
        """ If current position is on min level"""
        lvl = self.level(position)
        # odd levels are min levels
        return (lvl & 1) == 1

    def maxLevel(self, position):
        """ If current position is on max level"""
        lvl = self.level(position)
        # even levels are max levels
        return (lvl & 1) == 0

if __name__ == "__main__":
    optionString = 'Options \n 1. BuildMinMaxQueue 2. isEmpty 3. Size 4. GetMin 5. GetMax 6. put 7. RemoveMin 8. RemoveMax 9. isContain 10. exit \n Enter Choice: '
    mmQueue = MinMaxQueue()
    option = raw_input(optionString)
    option = int(option.rstrip())
    while (option != 10):
        if option == 1:
            filename = raw_input('Enter filename: ').rstrip()
            mmQueue.BuildMinMaxQueue(filename)
            print 'MinMaxQueue is built with elements of given file'
        if option == 2:
            print 'MinMaxQueue isEmpty', mmQueue.isEmpty()
        if option == 3:
            print 'Size of the MinMaxQueue is', mmQueue.Size()
        if option == 4:
            print 'minimum of MinMaxQueue is', mmQueue.GetMin()
        if option == 5:
            print 'maximum of MinMaxQueue is', mmQueue.GetMax()
        if option == 6:
            elem = raw_input('Enter element: ').rstrip()
            mmQueue.put(elem)
            print elem, 'is inserted to MinMaxQueue'
        if option == 7:
            minElm = mmQueue.RemoveMin()
            print 'minimum element', minElm, 'is removed'
        if option == 8:
            print 'maximum element', mmQueue.RemoveMax(), 'is removed'
        if option == 9:
            elem = raw_input('Enter element: ').rstrip()
            print 'isContain', elem, mmQueue.isContain(elem)
        option = int(raw_input(optionString).rstrip())

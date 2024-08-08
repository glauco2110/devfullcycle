package events

import (
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/suite"
)

type TestEvent struct {
	Name    string
	Payload interface{}
}

func (e *TestEvent) GetName() string {
	return e.Name
}

func (e *TestEvent) GetPayLoad() interface{} {
	return e.Payload
}

func (e *TestEvent) GetDateTime() time.Time {
	return time.Now()
}

type TestEventHandler struct {
	ID int
}

func (h *TestEventHandler) Handle(event EventInterface) {

}

type EventDispatcherTestSuit struct {
	suite.Suite
	event           TestEvent
	event2          TestEvent
	handler         TestEventHandler
	handler2        TestEventHandler
	handler3        TestEventHandler
	eventDispatcher EventDispatcher
}

func (suite *EventDispatcherTestSuit) SetupTest() {
	suite.eventDispatcher = *NewEventDispatcher()
	suite.handler = TestEventHandler{ID: 1}
	suite.handler2 = TestEventHandler{ID: 2}
	suite.handler3 = TestEventHandler{ID: 3}
	suite.event = TestEvent{Name: "test", Payload: "test"}
	suite.event2 = TestEvent{Name: "test2", Payload: "test2"}
}

func (suit *EventDispatcherTestSuit) TestEventDispatcher_Register() {
	err := suit.eventDispatcher.Register(suit.event.GetName(), &suit.handler)
	suit.Nil(err)
	suit.Equal(1, len(suit.eventDispatcher.handlers[suit.event.GetName()]))

	err = suit.eventDispatcher.Register(suit.event.GetName(), &suit.handler2)
	suit.Nil(err)
	suit.Equal(2, len(suit.eventDispatcher.handlers[suit.event.GetName()]))

	assert.Equal(suit.T(), &suit.handler, suit.eventDispatcher.handlers[suit.event.GetName()][0])
	assert.Equal(suit.T(), &suit.handler2, suit.eventDispatcher.handlers[suit.event.GetName()][1])

}

func (suite *EventDispatcherTestSuit) TestEventDispatcher_Register_WithSameHandler() {
	err := suite.eventDispatcher.Register(suite.event.GetName(), &suite.handler)
	suite.Nil(err)
	suite.Equal(1, len(suite.eventDispatcher.handlers[suite.event.GetName()]))

	err = suite.eventDispatcher.Register(suite.event.GetName(), &suite.handler)
	suite.Equal(ErrHandlerAlreadyRegitered, err)
	suite.Equal(1, len(suite.eventDispatcher.handlers[suite.event.GetName()]))

}

func (suit *EventDispatcherTestSuit) TestEventDispatcher_Clear() {
	err := suit.eventDispatcher.Register(suit.event.GetName(), &suit.handler)
	suit.Nil(err)
	suit.Equal(1, len(suit.eventDispatcher.handlers[suit.event.GetName()]))

	err = suit.eventDispatcher.Register(suit.event.GetName(), &suit.handler2)
	suit.Nil(err)
	suit.Equal(2, len(suit.eventDispatcher.handlers[suit.event.GetName()]))

	err = suit.eventDispatcher.Register(suit.event2.GetName(), &suit.handler3)
	suit.Nil(err)
	suit.Equal(1, len(suit.eventDispatcher.handlers[suit.event2.GetName()]))

	suit.eventDispatcher.Clear()
	suit.Equal(0, len(suit.eventDispatcher.handlers))
}

func (suit *EventDispatcherTestSuit) TestEventDispatcher_Has() {
	err := suit.eventDispatcher.Register(suit.event.GetName(), &suit.handler)
	suit.Nil(err)
	suit.Equal(1, len(suit.eventDispatcher.handlers[suit.event.GetName()]))

	err = suit.eventDispatcher.Register(suit.event.GetName(), &suit.handler2)
	suit.Nil(err)
	suit.Equal(2, len(suit.eventDispatcher.handlers[suit.event.GetName()]))

	assert.True(suit.T(), suit.eventDispatcher.Has(suit.event.GetName(), &suit.handler))
	assert.True(suit.T(), suit.eventDispatcher.Has(suit.event.GetName(), &suit.handler2))
	assert.False(suit.T(), suit.eventDispatcher.Has(suit.event.GetName(), &suit.handler3))

}

type MockHandler struct {
	mock.Mock
}

func (m *MockHandler) Handle(event EventInterface) {
	m.Called(event)
}

func (suite *EventDispatcherTestSuit) TestEventDispatcher_Dispatch() {
	eh := &MockHandler{}
	eh.On("Handle", &suite.event)
	suite.eventDispatcher.Register(suite.event.GetName(), eh)
	suite.eventDispatcher.Dispatch(&suite.event)
	eh.AssertExpectations(suite.T())
	eh.AssertNumberOfCalls(suite.T(), "Handle", 1)
}

func (suit *EventDispatcherTestSuit) TestEventDispatcher_Remove() {
	err := suit.eventDispatcher.Register(suit.event.GetName(), &suit.handler)
	suit.Nil(err)
	suit.Equal(1, len(suit.eventDispatcher.handlers[suit.event.GetName()]))

	err = suit.eventDispatcher.Register(suit.event.GetName(), &suit.handler2)
	suit.Nil(err)
	suit.Equal(2, len(suit.eventDispatcher.handlers[suit.event.GetName()]))

	err = suit.eventDispatcher.Register(suit.event2.GetName(), &suit.handler3)
	suit.Nil(err)
	suit.Equal(1, len(suit.eventDispatcher.handlers[suit.event2.GetName()]))

	suit.eventDispatcher.Remove(suit.event.GetName(), &suit.handler)
	suit.Equal(1, len(suit.eventDispatcher.handlers[suit.event.GetName()]))
	assert.Equal(suit.T(), &suit.handler2, suit.eventDispatcher.handlers[suit.event.GetName()][0])

	suit.eventDispatcher.Remove(suit.event.GetName(), &suit.handler2)
	suit.Equal(0, len(suit.eventDispatcher.handlers[suit.event.GetName()]))

	suit.eventDispatcher.Remove(suit.event2.GetName(), &suit.handler3)
	suit.Equal(0, len(suit.eventDispatcher.handlers[suit.event2.GetName()]))

}

func TestSuite(t *testing.T) {
	suite.Run(t, new(EventDispatcherTestSuit))
}

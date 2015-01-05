# encoding: utf-8
require "logstash/namespace"
require "logstash/logging"
require "metriks"

require "thread" # for SizedQueue
class LogStash::SizedQueue < SizedQueue
  attr_reader :push_meter
  attr_reader :pop_meter

  def initialize(max)
    @push_meter = Metriks.meter("push")
    @pop_meter = Metriks.meter("pop")
    super(max)
  end

  def push(obj)
    @push_meter.mark
    super(obj)
  end

  def pop(*args)
    @pop_meter.mark
    super
  end
end

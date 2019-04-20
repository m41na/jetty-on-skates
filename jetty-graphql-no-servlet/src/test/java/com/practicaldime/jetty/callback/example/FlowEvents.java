package com.practicaldime.jetty.callback.example;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.functions.Consumer;

public class FlowEvents {

	private IntSupplier supplier = new IntSupplier();
	private boolean stop;

	public FlowEvents() {
		super();
	}

	public static void main(String[] args) {
		FlowEvents app = new FlowEvents();
		app.start0();
	}

	public void start0() {
		ToFlowable toEvents = new ToFlowable() {

			@Override
			public void accept(IntEvent t) throws Exception {
				if(emitter != null) {
					emitter.onNext(t);
				}
			}
		};
		
		FlowableOnSubscribe<IntEvent> flowSub = new FlowableOnSubscribe<IntEvent>() {

			@Override
			public void subscribe(FlowableEmitter<IntEvent> emitter) throws Exception {
				System.out.println("Setting emitter");
				toEvents.setEmitter(emitter);
			}
		};
		
		Flowable<IntEvent> flowable = Flowable.create(flowSub, BackpressureStrategy.BUFFER);
		flowable.subscribe(i -> System.out.println(i.data()));
		
		EventListener<Integer> listener = new EventListener<Integer>() {

			@Override
			public void onEvent(Event<Integer> event) {
				try {
					toEvents.accept(new IntEvent(event.data()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		while (!stop) {
			try {
				Thread.sleep(1000);
				listener.onEvent(new IntEvent(supplier.get()));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public void start() {
		ToObservable toEvents = new ToObservable() {

			@Override
			public void accept(IntEvent t) throws Exception {
				if(emitter != null) {
					emitter.onNext(t);
				}
			}
		};

		Consumer<ObservableEmitter<IntEvent>> consumer = new Consumer<ObservableEmitter<IntEvent>>() {

			@Override
			public void accept(ObservableEmitter<IntEvent> emitter) throws Exception {
				System.out.println("Setting emitter");
				toEvents.setEmitter(emitter);
			}
		};

		Observable<IntEvent> obs = Observable.<IntEvent>create(emitter -> consumer.accept(emitter));
		obs.subscribe(i->System.out.println(i.data()));

		EventListener<Integer> listener = new EventListener<Integer>() {

			@Override
			public void onEvent(Event<Integer> event) {
				try {
					toEvents.accept(new IntEvent(event.data()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		while (!stop) {
			try {
				Thread.sleep(1000);
				listener.onEvent(new IntEvent(supplier.get()));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}

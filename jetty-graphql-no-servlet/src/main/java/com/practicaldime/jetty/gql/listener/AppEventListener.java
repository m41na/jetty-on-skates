package com.practicaldime.jetty.gql.listener;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.functions.Consumer;

public class AppEventListener<T> implements Consumer<T> {

	private ObservableEmitter<T> emitter;
	private final Consumer<ObservableEmitter<T>> consumer = new Consumer<ObservableEmitter<T>>() {

		@Override
		public void accept(ObservableEmitter<T> emitter) throws Exception {
			System.out.println("preparing emitter");
			setEmitter(emitter);
		}
	};

	private final Observable<T> observable = Observable.<T>create(emitter -> consumer.accept(emitter));

	public void setEmitter(ObservableEmitter<T> emitter) {
		this.emitter = emitter;
	}

	public Observable<T> getObservable() {
		return observable;
	}

	@Override
	public void accept(T t) throws Exception {
		if (emitter != null) {
			emitter.onNext(t);
		}
	}

	public void onEvent(T event) {
		try {
			accept(event);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			emitter.onComplete();
		}
	}
}

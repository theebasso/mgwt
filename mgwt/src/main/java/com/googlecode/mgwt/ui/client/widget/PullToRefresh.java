package com.googlecode.mgwt.ui.client.widget;

import java.util.Iterator;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.theme.base.PullToRefreshCss;
import com.googlecode.mgwt.ui.client.widget.scroll.ScrollEndEvent;
import com.googlecode.mgwt.ui.client.widget.scroll.ScrollEndHandler;
import com.googlecode.mgwt.ui.client.widget.scroll.ScrollEvent;
import com.googlecode.mgwt.ui.client.widget.scroll.ScrollHandler;
import com.googlecode.mgwt.ui.client.widget.scroll.ScrollStartEvent;
import com.googlecode.mgwt.ui.client.widget.scroll.ScrollStartHandler;

/**
 * Experimental don`t use right now
 * 
 * @author Daniel Kurka
 * 
 */
public class PullToRefresh extends Composite implements HasWidgets {
	private ScrollPanel scroll;
	private FlowPanel container;
	private RefreshWidget refreshWidget;
	private final PullToRefreshCss css;

	public PullToRefresh() {
		this(MGWTStyle.getDefaultClientBundle().getPullToRefreshCss());
	}

	public PullToRefresh(PullToRefreshCss css) {
		this.css = css;
		this.css.ensureInjected();
		scroll = new ScrollPanel();

		initWidget(scroll);

		scroll.addStyleName(MGWTStyle.getDefaultClientBundle().getLayoutCss().fillPanelExpandChild());

		FlowPanel main = new FlowPanel();
		scroll.setWidget(main);

		main.getElement().getStyle().setPosition(Position.RELATIVE);

		refreshWidget = new RefreshWidget(css);
		main.add(refreshWidget);

		scroll.addScrollhandler(refreshWidget);
		scroll.addScrollEndHandler(refreshWidget);

		container = new FlowPanel();
		main.add(container);
	}

	@Override
	public void add(Widget w) {
		container.add(w);

	}

	@Override
	public void clear() {
		container.clear();

	}

	@Override
	public Iterator<Widget> iterator() {
		return container.iterator();
	}

	@Override
	public boolean remove(Widget w) {
		return container.remove(w);
	}

	private class RefreshWidget extends Widget implements ScrollHandler, ScrollEndHandler, ScrollStartHandler {

		private Element main;

		private boolean startReload;

		private Element arrow;

		private Element text;

		public RefreshWidget(PullToRefreshCss css) {

			main = DOM.createDiv();
			main.addClassName(css.pullToRefresh());
			setElement(main);

			arrow = DOM.createDiv();
			arrow.addClassName(css.arrow());
			arrow.addClassName(css.arrowDown());
			main.appendChild(arrow);

			text = DOM.createDiv();
			text.addClassName(css.text());
			main.appendChild(text);

		}

		@Override
		public void onScrollEnd(ScrollEndEvent event) {
			if (startReload) {
				startLoading();
				startReload = false;
			}
			text.setInnerText("end: " + event.getX() + " " + event.getY());

		}

		@Override
		public void onScroll(ScrollEvent event) {
			if (event.getY() > 50) {
				text.setInnerText("release to reload: " + event.getX() + " " + event.getY());
				startReload = true;
				int degree = 90 - event.getY() - 50;
				arrow.setAttribute("style", "-webkit-transform: rotate(" + degree + "deg);");
			} else {
				text.setInnerText("scrolling: " + event.getX() + " " + event.getY());
				startReload = false;
			}

		}

		@Override
		public void onStartScroll(ScrollStartEvent event) {
			startReload = false;

		}
	}

	public void startLoading() {
		System.out.println("reload und so ");

	}
}
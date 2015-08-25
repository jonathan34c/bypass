package in.uncod.android.bypass;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import in.uncod.android.bypass.Element.Type;
import in.uncod.android.bypass.style.HorizontalLineSpan;

public class Bypass {
	static {
		System.loadLibrary("bypass");
	}

	private final Options mOptions;

	private final int mListItemIndent;
	private final int mBlockQuoteIndent;
	private final int mCodeBlockIndent;
	private final int mHruleSize;
	 Context context;
	private final int mHruleTopBottomPadding;
	private List<Integer> number=new ArrayList<Integer>();
	private List<String> typelist=new ArrayList<String>();
	private List<CharSequence> contentlist=new ArrayList<CharSequence>();
	private List<Integer> imagenumber=new ArrayList<Integer>();
	private List<String> imageurl=new ArrayList<String>();
	// Keeps track of the ordered list number for each LIST element.
	// We need to track multiple ordered lists at once because of nesting.
	private final Map<Element, Integer> mOrderedListNumber = new ConcurrentHashMap<Element, Integer>();
	private SpannableStringBuilder builder1=new SpannableStringBuilder();
	private static final String TAG="BYPASS";

	/**
	 * @deprecated Use {@link #Bypass(android.content.Context)} instead.
	 */
	@Deprecated
	public Bypass() {
		// Default constructor for backwards-compatibility
		mOptions = new Options();
		mListItemIndent = 20;
		mBlockQuoteIndent = 10;
		mCodeBlockIndent = 10;
		mHruleSize = 2;
		mHruleTopBottomPadding = 20;
	}

	public Bypass(Context context) {
		this(context, new Options());
		this.context=context;

	}

	public Bypass(Context context, Options options) {
		mOptions = options;

		DisplayMetrics dm = context.getResources().getDisplayMetrics();

		mListItemIndent = (int) TypedValue.applyDimension(mOptions.mListItemIndentUnit,
				mOptions.mListItemIndentSize, dm);

		mBlockQuoteIndent = (int) TypedValue.applyDimension(mOptions.mBlockQuoteIndentUnit,
				mOptions.mBlockQuoteIndentSize, dm);

		mCodeBlockIndent = (int) TypedValue.applyDimension(mOptions.mCodeBlockIndentUnit,
				mOptions.mCodeBlockIndentSize, dm);

		mHruleSize = (int) TypedValue.applyDimension(mOptions.mHruleUnit,
				mOptions.mHruleSize, dm);

		mHruleTopBottomPadding = (int) dm.density * 10;
	}
//
//	public CharSequence markdownToSpannable(String markdown) {
//		//return markdownToSpannable(markdown, null);
//
//
//		return markdownToSpannable(markdown, new Html.ImageGetter() {
//
//			public Drawable getDrawable(String source) {
//				Log.w(TAG, "has drawable");
//
//				ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//						.build();
//				ImageLoader.getInstance().init(config);
//				Drawable drawable = null;
//				URL url;
//
//				try {
//					url = new URL(source);
//					Bitmap bmp = ImageLoader.getInstance().loadImageSync(url.toString());
//					drawable = new BitmapDrawable(bmp);
//					//drawable = Drawable.createFromStream(url.openStream(), null);
//					Log.w("log", "from string");
//				} catch (Exception e) {
//					e.printStackTrace();
//					Log.w("log", "e from string");
//					return null;
//				}
//				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//				//return context.getResources().getDrawable(R.drawable.ic_launcher);
//				return drawable;
//			}
//
//		});
//	}

	public CharSequence markdownToSpannable(String markdown) {
		return markdownToSpannable(markdown, new Html.ImageGetter() {

			public Drawable getDrawable(String source) {
				Drawable drawable = null;
				URL url;
				try {
					url = new URL(source);
					drawable = Drawable.createFromStream(url.openStream(), null);
					Log.w("log", "from string");
				} catch (Exception e) {
					e.printStackTrace();
					Log.w("log", "e from string");
					return null;
				}
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
				return drawable;
			}

		});
	}

	public CharSequence markdownToSpannable(String markdown, Html.ImageGetter getter) {
		Document document = processMarkdown(markdown);
		CharSequence[] spans = new CharSequence[document.getElementCount()];
		Log.w("elemnetcount",String.valueOf(document.getElementCount()));

		for (int i = 0; i < document.getElementCount(); i++) {
			spans[i] = recurseElement(document.getElement(i), getter ,i);
			this.typelist.add(document.getElement(i).getType().toString());

			this.contentlist.add(TextUtils.concat(spans[i]));


//			if(document.getElement(i).getType().toString().equals("IMAGE")){
//				this.contentlist.add(document.getElement(i).getAttribute("link"));
//				Log.w(TAG, "typeimageyes" );
//				Log.w(TAG, "content" +document.getElement(i).getAttribute("link") );
//			}else{
//				this.contentlist.add(TextUtils.concat(spans[i]));
//				Log.w(TAG, "content" + TextUtils.concat(spans[i]));
//
//			}


			Log.w(TAG, "LOOPTYPE" +"  " +document.getElement(i).getType().toString());
			Log.w(TAG, "LOOPCONTENT" +"  " +spans[i].toString());

		}
		//contentlist.add(builder);

		return TextUtils.concat(spans);


	}

	private native Document processMarkdown(String markdown);

	private CharSequence recurseElement(Element element, Html.ImageGetter igetter, int position) {
		Type type = element.getType();
		boolean isOrderedList = false;


		//this.typelist.add(element.getType().toString());




		if (type == Type.LIST) {
			String flagsStr = element.getAttribute("flags");
			int flags = Integer.parseInt(flagsStr);
			isOrderedList = (flags & Element.F_LIST_ORDERED) != 0;
			if (isOrderedList) {
				mOrderedListNumber.put(element, 1);
			}
		}
//		Log.w("log_colorr",element.getAttribute("color"));

		//Log.w("link_tagr",element.getAttribute("title"));


		CharSequence[] spans = new CharSequence[element.size()];
		for (int i = 0; i < element.size(); i++) {
			spans[i] = recurseElement(element.children[i], getter, position);
			Log.w(TAG, "ELEMENTtype" + element.getType().toString());
			Log.w(TAG, "ELEMENTattribute" + element.getAttribute("link"));
			Log.w(TAG, "ELEMENTconent" + element.children[i].toString());

		}

		// Clean up after we're done
		if (isOrderedList) {
			mOrderedListNumber.remove(this);
		}

		CharSequence concat = TextUtils.concat(spans);

		SpannableStringBuilder builder = new ReverseSpannableStringBuilder();

		String text = element.getText();

		if (element.size() == 0 && element.getParent() != null && element.getParent().getType() != Type.BLOCK_CODE) {
			  //text = text.replace('\n', ' ');


		}

		// Retrieve the image now so we know whether we're going to have something to display later
		// If we don't, then show the alt text instead (if available).

		Drawable imageDrawable = null;

		if (type == Type.IMAGE && getter != null && !TextUtils.isEmpty(element.getAttribute("link"))) {

			//imageDrawable=getter.getDrawable(element.getAttribute("link"));

			imageurl.add(element.getAttribute("link"));


			//this.contentlist.add(element.getAttribute("link"));
//			imageDrawable=context.getResources().getDrawable(R.drawable.ic_launcher);
//			if(imageDrawable==null){
//				Log.w(TAG, "imagenull");
//			}else{
//				Log.w(TAG, "notnull");
//			}


		}

		Log.w("TYPE", type.toString());
		switch (type) {
			case LIST:
				if (element.getParent() != null
						&& element.getParent().getType() == Type.LIST_ITEM) {
					builder.append("\n");
				}
				break;
			case LINEBREAK:
				builder.append("\n");
				break;
			case LIST_ITEM:
				builder.append(" ");
				if (mOrderedListNumber.containsKey(element.getParent())) {
					int number = mOrderedListNumber.get(element.getParent());
					builder.append(Integer.toString(number) + ".");
					mOrderedListNumber.put(element.getParent(), number + 1);
				}
				else {
					builder.append(mOptions.mUnorderedListItem);
				}
				builder.append("  ");
				break;
			case AUTOLINK:

//				String type1=element.getAttribute("type");
//				Log.w("linktype",type1);
				builder.append(element.getAttribute("link"));

				break;
			case HRULE:
				// This ultimately gets drawn over by the line span, but
				// we need something here or the span isn't even drawn.
				builder.append("-");
				break;
			case IMAGE:
				// Display alt text (or title text) if there is no image
				if (imageDrawable == null) {
					String show = element.getAttribute("alt");
					if (TextUtils.isEmpty(show)) {
						show = element.getAttribute("title");
					}
					if (!TextUtils.isEmpty(show)) {
						show = "[" + show + "]";
						builder.append(show);
					}
				}
				else {
					// Character to be replaced
					Log.w("log","reolace space");
					builder.append("\uFFFC");
					Log.w("imageposition", String.valueOf(position));

				}
				break;
			case COLOR:
				// This ultimately gets drawn over by the line span, but
				// we need something here or the span isn't even drawn.
				Log.w("typecolor", "color");
				Log.w("COLOR", "link"+element.getAttribute("link"));
				Log.w("COLOR", "title"+element.getAttribute("title"));
				Log.w("COLOR", "alt" + element.getAttribute("alt"));

				//String colortxt=element.getAttribute("link");
				builder.append(element.getAttribute("title"));
				//builder.append(Html.fromHtml(element.getAttribute("link")));
				break;

		}

			builder.append(text);
			builder.append(concat);


		if (type == Type.LIST_ITEM) {
			if (element.size() == 0 || !element.children[element.size() - 1].isBlockElement()) {
				builder.append("\n");
			}
		}
		else if (element.isBlockElement() && type != Type.BLOCK_QUOTE) {
			if (type == Type.LIST) {
				// If this is a nested list, don't include newlines
				if (element.getParent() == null || element.getParent().getType() != Type.LIST_ITEM) {
					builder.append("\n");
				}
			}
			else if (element.getParent() != null
					&& element.getParent().getType() == Type.LIST_ITEM) {
				// List items should never double-space their entries
				builder.append("\n");
			}
			else {
				builder.append("\n\n");
			}
		}

		switch (type) {

			case HEADER:

				String levelStr = element.getAttribute("level");
				int level = Integer.parseInt(levelStr);
				setSpan(builder, new RelativeSizeSpan(mOptions.mHeaderSizes[level - 1]));
				setSpan(builder, new StyleSpan(Typeface.BOLD));
				this.number.add(position);
				Log.w(TAG, "header" + this.builder1.toString());
//				this.typelist.add(type.toString());
//				this.contentlist.add(builder);

				break;
			case LIST:
				setBlockSpan(builder, new LeadingMarginSpan.Standard(mListItemIndent));
				this.number.add(position);
//				this.contentlist.add(builder);
//				this.typelist.add(type.toString());


				break;
			case EMPHASIS:
				setSpan(builder, new StyleSpan(Typeface.ITALIC));
				this.number.add(position);
//				this.contentlist.add(builder);
//				this.typelist.add(type.toString());

				break;
			case DOUBLE_EMPHASIS:
				setSpan(builder, new StyleSpan(Typeface.BOLD));
				this.number.add(position);
//				this.contentlist.add(builder);
//				this.typelist.add(type.toString());

				Log.w("log", "double");
				break;
			case TRIPLE_EMPHASIS:
				Log.w("text", builder.toString());
				setSpan(builder, new StyleSpan(Typeface.BOLD_ITALIC));
				this.number.add(position);
//				this.contentlist.add(builder);
//				this.typelist.add(type.toString());

				break;
			case BLOCK_CODE:
				setSpan(builder, new LeadingMarginSpan.Standard(mCodeBlockIndent));
				setSpan(builder, new TypefaceSpan("monospace"));
				this.number.add(position);
//				this.contentlist.add(builder);
//				this.typelist.add(type.toString());
////

				break;
			case CODE_SPAN:
				setSpan(builder, new TypefaceSpan("monospace"));
				this.number.add(position);
//				this.contentlist.add(builder);
//				this.typelist.add(type.toString());

				break;
			case LINK:
				this.number.add(position);
//				this.contentlist.add(builder);
//				this.typelist.add(type.toString());

				break;
			case AUTOLINK:
				Log.w("log", "autolink");
				//Log.w("log", element.getAttribute("link"));
				setSpan(builder, new URLSpan(element.getAttribute("link")));
				Log.w("log", element.getAttribute("link"));
				this.number.add(position);
//				this.contentlist.add(builder);
//				this.typelist.add(type.toString());

				break;
			case BLOCK_QUOTE:
				Log.w("log","quote");
				// We add two leading margin spans so that when the order is reversed,
				// the QuoteSpan will always be in the same spot.
				setBlockSpan(builder, new LeadingMarginSpan.Standard(mBlockQuoteIndent));
				setBlockSpan(builder, new QuoteSpan(mOptions.mBlockQuoteColor));
				setBlockSpan(builder, new LeadingMarginSpan.Standard(mBlockQuoteIndent));
				setBlockSpan(builder, new StyleSpan(Typeface.ITALIC));
				this.number.add(position);
//				this.contentlist.add(builder);;
//				this.typelist.add(type.toString());

				break;
			case STRIKETHROUGH:
				setSpan(builder, new StrikethroughSpan());
				this.number.add(position);
//				this.contentlist.add(builder);
//				this.typelist.add(type.toString());

				break;
			case BLOCK_HTML:
				Log.w("log","html_block");
				Log.w("text", builder.toString());
				setSpan(builder, new StyleSpan(Typeface.BOLD_ITALIC));
				this.number.add(position);
//				this.contentlist.add(builder);
//				this.typelist.add(type.toString());

				break;
			case HRULE:
				setSpan(builder, new HorizontalLineSpan(mOptions.mHruleColor, mHruleSize, mHruleTopBottomPadding));
				this.number.add(position);
//				this.contentlist.add(builder);
//				this.typelist.add(type.toString());

				break;
			case IMAGE:
				Log.w("log", "show1");
				//setSpan(builder, new ImageSpan(imageDrawable));
				if (imageDrawable != null) {
					Log.w("log","show2");
					setSpan(builder, new ImageSpan(imageDrawable));
				}
				this.number.add(position);
				this.contentlist.add(element.getAttribute("link"));
				this.typelist.add(type.toString());
				imagenumber.add(position);

				break;
			case COLOR:
				// This ultimately gets drawn over by the line span, but
				// we need something here or the span isn't even drawn.
				Log.w("typecolor", "color");
				Log.w("log_color", element.getAttribute("link"));
				Log.w("log_color_title", element.getAttribute("title"));
				//Log.w("alt", element.getAttribute("alt"));
				//setSpan(builder, new StyleSpan(Color.RED));
				setSpan(builder, new ForegroundColorSpan(Color.parseColor(element.getAttribute("alt"))));
				//setSpan(builder, new ForegroundColorSpan(Color.parseColor(element.getAttribute("title"))));
				//setSpan(Html.fromHtml(color), TextView.BufferType.SPANNABLE);
				//setSpan(builder, new ForegroundColorSpan(Html.fromHtml()));
				//setSpan(builder, new ForegroundColorSpan(Color.RED));
				//setSpan(builder, new StrikethroughSpan());
				this.number.add(position);

				break;
			case TEXT:

				this.number.add(position);
//				this.contentlist.add(builder);
//				this.typelist.add(type.toString());


				break;
			case PARAGRAPH:
				Log.w("PARAGRAPH", builder.toString());
				String pargraph=concat.toString();
				if(pargraph.startsWith("*")&&pargraph.endsWith("*")||pargraph.startsWith("**")&&pargraph.endsWith("**")||pargraph.startsWith("***")&&pargraph.endsWith("***")){
					Log.w("PARAGRAPH", String.valueOf(builder.length()));
					if(pargraph.startsWith("***")||pargraph.endsWith("***")){
						builder.replace(0, 3, "");
						Log.w("CHARACOTR", builder.subSequence(0, builder.length()).toString());
						builder.replace(builder.length() - 5, builder.length(), "");

						setSpan(builder, new StyleSpan(Typeface.BOLD_ITALIC));
					}else if(pargraph.startsWith("**")||pargraph.endsWith("**")){

						builder.replace(0, 2, "");
						Log.w("CHARACOTR", builder.subSequence(0, builder.length()).toString());
						builder.replace(builder.length() - 4, builder.length(), "");

						setSpan(builder, new StyleSpan(Typeface.BOLD));
					}else if(pargraph.startsWith("*")||pargraph.endsWith("*")) {
						builder.replace(0, 1, "");
						Log.w("CHARACOTR", builder.subSequence(0, builder.length()).toString());
						builder.replace(builder.length()-3, builder.length(), "");
						//builder.replace(builder.length(),builder.length(),"");
						setSpan(builder, new StyleSpan(Typeface.ITALIC));
					}
				}


				break;
		}
//		Log.w(TAG, "type"+type.toString());
//		Log.w(TAG, builder.toString());
		return builder;



	}

	private static void setSpan(SpannableStringBuilder builder, Object what) {
		builder.setSpan(what, 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	// These have trailing newlines that we want to avoid spanning
	private static void setBlockSpan(SpannableStringBuilder builder, Object what) {

		builder.setSpan(what, 0, builder.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	/**
	 * Configurable options for how Bypass renders certain elements.
	 */
	public static final class Options {
		private float[] mHeaderSizes;

		private String mUnorderedListItem;
		private int mListItemIndentUnit;
		private float mListItemIndentSize;

		private int mBlockQuoteColor;
		private int mBlockQuoteIndentUnit;
		private float mBlockQuoteIndentSize;

		private int mCodeBlockIndentUnit;
		private float mCodeBlockIndentSize;

		private int mHruleColor;
		private int mHruleUnit;
		private float mHruleSize;

		public Options() {
			mHeaderSizes = new float[] {
					1.5f, // h1
					1.4f, // h2
					1.3f, // h3
					1.2f, // h4
					1.1f, // h5
					1.0f, // h6
			};

			mUnorderedListItem = "\u2022";
			mListItemIndentUnit = TypedValue.COMPLEX_UNIT_DIP;
			mListItemIndentSize = 10;

			mBlockQuoteColor = 0xff0000ff;
			mBlockQuoteIndentUnit = TypedValue.COMPLEX_UNIT_DIP;
			mBlockQuoteIndentSize = 10;

			mCodeBlockIndentUnit = TypedValue.COMPLEX_UNIT_DIP;
			mCodeBlockIndentSize = 10;

			mHruleColor = Color.GRAY;
			mHruleUnit = TypedValue.COMPLEX_UNIT_DIP;
			mHruleSize = 1;
		}

		public Options setHeaderSizes(float[] headerSizes) {
			if (headerSizes == null) {
				throw new IllegalArgumentException("headerSizes must not be null");
			}
			else if (headerSizes.length != 6) {
				throw new IllegalArgumentException("headerSizes must have 6 elements (h1 through h6)");
			}

			mHeaderSizes = headerSizes;

			return this;
		}

		public Options setUnorderedListItem(String unorderedListItem) {
			mUnorderedListItem = unorderedListItem;
			return this;
		}

		public Options setListItemIndentSize(int unit, float size) {
			mListItemIndentUnit = unit;
			mListItemIndentSize = size;
			return this;
		}

		public Options setBlockQuoteColor(int color) {
			mBlockQuoteColor = color;
			return this;
		}

		public Options setBlockQuoteIndentSize(int unit, float size) {
			mBlockQuoteIndentUnit = unit;
			mBlockQuoteIndentSize = size;
			return this;
		}

		public Options setCodeBlockIndentSize(int unit, float size) {
			mCodeBlockIndentUnit = unit;
			mCodeBlockIndentSize = size;
			return this;
		}

		public Options setHruleColor(int color) {
			mHruleColor = color;
			return this;
		}

		public Options setHruleSize(int unit, float size) {
			mHruleUnit = unit;
			mHruleSize = size;
			return this;
		}
	}

	/**
	 * Retrieves images for markdown images.
	 */


	//get image position
	public List<Integer> getcount(){


		return number;

	}
	public List<String> gettype(){


		return typelist;

	}
	public List<Integer> getimgnumber(){


		return imagenumber;

	}
	public List<CharSequence> getcontent(){


		return contentlist;

	}
	public List<String> geturl(){


		return imageurl;

	}
	public static interface ImageGetter {

		/**
		 * This method is called when the parser encounters an image tag.
		 */
		public Drawable getDrawable(String source);

	}
	public static Html.ImageGetter getter = new Html.ImageGetter() {

		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			URL url;
			try {
				url = new URL(source);
				drawable = Drawable.createFromStream(url.openStream(), null);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			return drawable;
		}

	};


}
# PetCare Pro — UI direction and screen inventory

The visual system is designed to feel reassuring, personal, and lively without using alarm-heavy colors or overstimulating decoration. The palette uses a deep botanical green for primary actions, a warm near-white canvas for long sessions, soft mint surfaces, and restrained marigold/coral accents. Text and status colors remain distinct so important care information is easy to scan.

## Design tokens

| Role | Color | Usage |
|---|---|---|
| Forest | `#145C50` | Primary actions, selected navigation, key headings |
| Mint | `#E5F1EB` | Soft cards, icon backplates, secondary surfaces |
| Canvas | `#F7F8F4` | Main light-mode background |
| Paper | `#FFFFFF` | Cards and form surfaces |
| Ink | `#1E2B27` | Primary text |
| Muted ink | `#5F6F69` | Supporting labels |
| Marigold | `#FFD18A` | Small positive highlights and featured details |
| Terracotta | `#9E493A` | Secondary emphasis; avoid using for large backgrounds |
| Danger | `#B3261E` | Destructive/error states only |

Dark mode uses a deep green-charcoal surface, pale mint primary actions, and warm readable text. Color is never the only way to communicate status; pair it with words or icons.

## Shared interaction rules

- Use a consistent 8/12/18/24/30 dp corner scale and comfortable 16–24 dp content spacing.
- Keep primary actions visible and labels explicit; icon-only controls need accessible descriptions.
- Forms open as modal bottom sheets for quick creation/edit actions, with a clear title, scrollable fields, keyboard-safe layout, validation, cancel/dismiss behavior, and a full-width primary save action.
- Use confirmation for destructive operations and show empty, loading, error, and permission-denied states.
- Respect system font scaling, screen insets, TalkBack semantics, and reduced-motion expectations.

## Screen inventory and target composition

| Screen | Visual composition and key interaction |
|---|---|
| Splash | Calm brand mark on a warm canvas, short transition, no prolonged gate before sign-in. |
| Login | Clear welcome heading, email/password fields, inline validation, password visibility, strong forest sign-in action, and a separate create-account link. |
| Registration | Scrollable, well-spaced labeled fields, password guidance, inline errors, and one primary create-account action. |
| Home | Pet selector and profile card; next care item; quick access to health, activity, nutrition, and nearby vet; bottom navigation for the primary areas. Add/log actions use bottom sheets. |
| Health overview | Compact status summary and separate vaccine, medication, and visit cards; each action routes to its history or opens a focused bottom sheet. |
| Vet visit history | Date-led timeline cards with clinic, visit reason, notes, edit action, and a bottom-sheet add/edit form. |
| Vaccination history | Due/completed sections, due-date badges with text labels, vaccine details, and bottom-sheet record form. |
| Medication history | Medication name, schedule and start date in scannable cards; add/edit in a scrollable bottom sheet; confirm deletion. |
| Reminders | Today/upcoming grouping, schedule and recurrence shown clearly, completion state, and bottom-sheet create/edit form. |
| Activity, grooming, behavior | Segmented category selector, readable timeline cards, empty states, and bottom-sheet forms for each entry type. |
| Nutrition | Pet-specific overview, concise informational guidance, links to diet and supplements; avoid medical claims. |
| Diet plan | Clear meal schedule and user-entered plan details; portion information is labeled as informational and prompts veterinary review. |
| Supplements | Neutral catalog-style cards and user records; avoid implying a supplement is a veterinary recommendation. |
| Vet locator | Map with a companion clinic list, accessible call/directions actions, and explicit location loading/permission/error states. |

## Bottom-sheet pattern

Use `ModalBottomSheet` for short, contextual tasks (add reminder, log activity, add health record, edit a care item). Sheets should not replace full-screen navigation for complex workflows such as account creation or detailed record browsing. Keep sheet state local to the screen or hoist it into a state holder when multiple destinations need it.

## Review checklist

- Verify all routes in both light and dark themes.
- Check contrast and large text/font scaling, TalkBack labels, keyboard overlap, and small-device scroll behavior.
- Confirm form save/cancel and back-dismiss behavior.
- Verify records persist and are associated with the selected pet before describing the product as production-ready.
- Capture emulator screenshots for each route at phone and large-text sizes before release.
